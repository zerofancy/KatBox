package top.ntutn.katbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatAreaViewModel: ViewModel() {
    private val _historyStateFlow = MutableStateFlow("")
    val historyStateFlow: StateFlow<String> = _historyStateFlow

    private val _modelsStateFlow = MutableStateFlow<List<Model>>(emptyList())
    private val _selectedModel = MutableStateFlow<Model?>(null)
    val selectedModel: StateFlow<Model?> = _selectedModel
    val modelsStateFlow: StateFlow<List<Model>> = _modelsStateFlow

    fun fetchModels() = viewModelScope.launch {
        val response = getPlatform().httpClient.get("http://localhost:11434/api/tags")
        val models = response.body<OllamaModelsResponse>()
        _modelsStateFlow.value = models.models
        _selectedModel.value = models.models.firstOrNull()

        _historyStateFlow.value += "Server: $models"
    }

    fun selectModel(model: Model) = viewModelScope.launch {
        _selectedModel.value = model
    }

    fun sendMessage(value: String) {
        _historyStateFlow.value += "User: $value\n"
        viewModelScope.launch {
            val response = getPlatform().httpClient.get("https://www.baidu.com/s?wd=$value")
            val responseText = response.bodyAsText()
            _historyStateFlow.value += "Server: $responseText\n"
        }
    }
}