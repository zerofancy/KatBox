package top.ntutn.katbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatAreaViewModel: ViewModel() {
    private val _historyStateFlow = MutableStateFlow("")
    val historyStateFlow: StateFlow<String> = _historyStateFlow

    fun sendMessage(value: String) {
        _historyStateFlow.value += "User: $value\n"
        viewModelScope.launch {
            val response = getPlatform().httpClient.get("https://www.baidu.com/s?wd=$value")
            val responseText = response.bodyAsText()
            _historyStateFlow.value += "Server: $responseText\n"
        }
    }
}