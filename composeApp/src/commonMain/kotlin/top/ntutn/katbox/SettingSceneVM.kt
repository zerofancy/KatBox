package top.ntutn.katbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import top.ntutn.katbox.storage.ConnectionDataStore

class SettingSceneVM(private val dataStore: ConnectionDataStore): ViewModel() {
    private val _inputtingUrl = MutableStateFlow("")
    val inputtingUrl: StateFlow<String> = _inputtingUrl

    fun onInit() {
        viewModelScope.launch {
            _inputtingUrl.value = dataStore.connectionData().first().url
        }
    }

    fun onInputChange(newValue: String) {
        _inputtingUrl.value = newValue
    }

    fun saveData() {
        viewModelScope.launch {
            dataStore.update(inputtingUrl.value)
        }
    }
}