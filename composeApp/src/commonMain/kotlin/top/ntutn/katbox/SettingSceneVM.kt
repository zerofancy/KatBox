package top.ntutn.katbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import top.ntutn.katbox.storage.ConnectionDataStore
import top.ntutn.katbox.storage.ModelType
import top.ntutn.katbox.storage.OllamaModelSetting

class SettingSceneVM(private val dataStore: ConnectionDataStore): ViewModel() {
    private val _inputtingUrl = MutableStateFlow("")
    val inputtingUrl: StateFlow<String> = _inputtingUrl

    fun onInit() {
        viewModelScope.launch {
            _inputtingUrl.value = dataStore.connectionData().firstOrNull()
                ?.settingMap[ModelType.OLLAMA]
                ?.let { it as? OllamaModelSetting }
                ?.url ?: ""
        }
    }

    fun onInputChange(newValue: String) {
        _inputtingUrl.value = newValue
    }

    fun saveData() {
        viewModelScope.launch {
            dataStore.updateOllama(inputtingUrl.value)
        }
    }
}