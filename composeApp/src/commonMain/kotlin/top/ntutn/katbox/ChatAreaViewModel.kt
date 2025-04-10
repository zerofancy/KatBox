package top.ntutn.katbox

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatAreaViewModel: ViewModel() {
    private val _historyStateFlow = MutableStateFlow("")
    val historyStateFlow: StateFlow<String> = _historyStateFlow

    fun sendMessage(value: String) {
        _historyStateFlow.value += "User: $value\n"
    }
}