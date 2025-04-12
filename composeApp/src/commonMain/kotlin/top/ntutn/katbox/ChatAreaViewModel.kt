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
import top.ntutn.katbox.logger.slf4jLogger
import top.ntutn.katbox.model.GenerateRequest
import top.ntutn.katbox.model.GenerateResponse
import top.ntutn.katbox.model.Model
import top.ntutn.katbox.model.OllamaModelsResponse

class ChatAreaViewModel : ViewModel() {
    private val _historyStateFlow = MutableStateFlow("")
    val historyStateFlow: StateFlow<String> = _historyStateFlow
    private val logger by slf4jLogger("vm")

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
            logger.error("fetch model failed", it)
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
        generateResponseJob?.apply {
            _historyStateFlow.value += "..Canceled\n"
            cancel()
        }
        _historyStateFlow.value += "User: $value\n"
        generateResponseJob = viewModelScope.launch {
            try {
                generateResponse(value)
            } catch (e: Exception) {
                logger.error("Generate response failed", e)
                _historyStateFlow.value += "..Failed\n"
            } finally {
                generateResponseJob = null
            }
        }
    }

    private suspend fun generateResponse(value: String) {
        val selectedModel = selectedModel.value
        if (selectedModel == null) {
            _historyStateFlow.value += "System: No Model Selected"
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
        _historyStateFlow.value += "Server: "
        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: break
            logger.debug("received $line")
            val generateResponse = withContext(Dispatchers.Default) {
                json.decodeFromString<GenerateResponse>(line)
            }
            _historyStateFlow.value += generateResponse.response
            generateResponse.context?.let {
                generateContext = it
            }
        }
        _historyStateFlow.value += "\n"
    }
}