package top.ntutn.katbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.ntutn.katbox.logger.loggerFacade
import top.ntutn.katbox.model.ChatMessage
import top.ntutn.katbox.model.GenerateRequest
import top.ntutn.katbox.model.GenerateResponse
import top.ntutn.katbox.model.Model
import top.ntutn.katbox.model.OllamaModelsResponse

class ChatAreaViewModel : ViewModel() {
    private val _historyStateFlow = MutableStateFlow(listOf<ChatMessage>())
    val historyStateFlow: StateFlow<List<ChatMessage>> = _historyStateFlow
    private val _composingMessage = MutableStateFlow<ChatMessage?>(null)
    val composingMessage: StateFlow<ChatMessage?> = _composingMessage
    private val logger by loggerFacade("vm")

    private val _modelsStateFlow = MutableStateFlow<List<Model>>(emptyList())
    private val _selectedModel = MutableStateFlow<Model?>(null)
    val selectedModel: StateFlow<Model?> = _selectedModel
    val modelsStateFlow: StateFlow<List<Model>> = _modelsStateFlow

    private var generateResponseJob: Job? = null
    private var generateContext: List<Int>? = null

    fun fetchModels() = viewModelScope.launch {
        val response = runCatching {
            getPlatform().httpClient.get("http://localhost:11434/api/tags")
        }.onFailure {
            logger.error{ log("fetch model failed", it) }
        }.getOrNull()
        val models = response?.body<OllamaModelsResponse>()
        _modelsStateFlow.value = models?.models ?: emptyList()
        _selectedModel.value = models?.models?.firstOrNull()
        generateContext = null
    }

    fun selectModel(model: Model) = viewModelScope.launch {
        if (model != _selectedModel.value) {
            generateContext = null
            _selectedModel.value = model
        }
    }

    fun sendMessage(value: String) = viewModelScope.launch {
        generateResponseJob?.cancel()
        composingMessage.value?.let {
            _historyStateFlow.value += it
        }
        _composingMessage.value = null
        _historyStateFlow.value += ChatMessage(
            timestamp = System.currentTimeMillis(),
            text = value,
            role = "User",
            completed = true
        )
        generateResponseJob = viewModelScope.launch {
            try {
                generateResponse(value)
            } catch (e: Exception) {
                logger.error{ log("Generate response failed", e) }
            } finally {
                generateResponseJob = null
            }
        }
    }

    private suspend fun generateResponse(value: String) {
        val selectedModel = selectedModel.value
        if (selectedModel == null) {
            _composingMessage.value = ChatMessage(
                timestamp = System.currentTimeMillis(),
                text = "No Selected Model",
                role = "System",
                completed = true
            )
            return
        }
        val request = GenerateRequest(
            model = selectedModel.name,
            value,
            true,
            generateContext
        )
        val statement = getPlatform().httpClientWithoutTimeout.preparePost("http://localhost:11434/api/generate") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val channel = statement.body<ByteReadChannel>()
        val json = getPlatform().jsonClient
        _composingMessage.value = ChatMessage(
            timestamp = System.currentTimeMillis(),
            text = "",
            role = "Assistant",
            completed = false
        )
        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: break
            logger.debug { log("received $line") }
            val generateResponse = withContext(Dispatchers.Default) {
                json.decodeFromString<GenerateResponse>(line)
            }
            _composingMessage.value = _composingMessage.value?.let {
                it.copy(text = it.text + generateResponse.response)
            }
            generateResponse.context?.let {
                generateContext = it
            }
        }
        _composingMessage.value = _composingMessage.value?.copy(completed = true)
    }
}