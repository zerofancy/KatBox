package top.ntutn.katbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
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

    fun fetchModels() = viewModelScope.launch {
        val response = runCatching {
            getPlatform().httpClient.get("http://localhost:11434/api/tags")
        }.onFailure {
            logger.error("fetch model failed", it)
        }.getOrNull()
        val models = response?.body<OllamaModelsResponse>()
        _modelsStateFlow.value = models?.models ?: emptyList()
        _selectedModel.value = models?.models?.firstOrNull()
    }

    fun selectModel(model: Model) = viewModelScope.launch {
        _selectedModel.value = model
    }

    fun sendMessage(value: String) = viewModelScope.launch {
        _historyStateFlow.value += "User: $value\n"
        val selectedModel = selectedModel.value
        if (selectedModel == null) {
            _historyStateFlow.value += "System: No Model Selected"
            return@launch
        }
        val request = GenerateRequest(
            model = selectedModel.name,
            value,
            false
        )
        val response = kotlin.runCatching {
            getPlatform().httpClient.post("http://localhost:11434/api/generate") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }.onFailure {
            logger.error("generate response failed", it)
        }.getOrNull()

        val generateResponse = response?.body<GenerateResponse>()
        _historyStateFlow.value += "Server: ${generateResponse?.response}\n"
    }
}