package top.ntutn.katbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import top.ntutn.katbox.storage.ConnectionDataStore
import top.ntutn.katbox.storage.DeepseekModelSetting
import top.ntutn.katbox.storage.ModelType

class DeepseekSettingSceneVM(private val dataStore: ConnectionDataStore): ViewModel() {
    private val _inputtingKey = MutableStateFlow("")
    val inputtingKey: StateFlow<String> = _inputtingKey

    fun onInit() {
        viewModelScope.launch {
            _inputtingKey.value = dataStore.connectionData().firstOrNull()
                ?.settingMap[ModelType.DEEPSEEK]
                ?.let { it as? DeepseekModelSetting }
                ?.key ?: ""
        }
    }

    fun onInputChange(newValue: String) {
        _inputtingKey.value = newValue
    }

    fun saveData() {
        viewModelScope.launch {
            dataStore.updateCurrentModel(ModelType.DEEPSEEK)
            dataStore.updateDeepseek(inputtingKey.value)
        }
    }
}