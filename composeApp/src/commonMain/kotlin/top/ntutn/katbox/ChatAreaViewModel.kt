package top.ntutn.katbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import top.ntutn.katbox.logger.loggerFacade
import top.ntutn.katbox.model.ChatMessage
import top.ntutn.katbox.model.ChatSessionProvider
import top.ntutn.katbox.model.Role
import top.ntutn.katbox.model.deepseek.DeepseekSessionProvider
import top.ntutn.katbox.storage.ConnectionDataStore

class ChatAreaViewModel(dataStore: ConnectionDataStore) : ViewModel() {
    private val _historyStateFlow = MutableStateFlow(listOf<ChatMessage>())
    val historyStateFlow: StateFlow<List<ChatMessage>> = _historyStateFlow
    private val _composingMessage = MutableStateFlow<ChatMessage?>(null)
    val composingMessage: StateFlow<ChatMessage?> = _composingMessage
    private val logger by loggerFacade("vm")

    private val _modelsStateFlow = MutableStateFlow<List<String>>(emptyList())
    private val _selectedModel = MutableStateFlow<String?>(null)
    val selectedModel: StateFlow<String?> = _selectedModel
    val modelsStateFlow: StateFlow<List<String>> = _modelsStateFlow

    private var generateContext: List<Int>? = null
    private var baseUrl = ""
    private var provider: ChatSessionProvider? = null
    private var providerScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        dataStore.connectionData().onEach {
            if (it.url != baseUrl) {
                baseUrl = it.url
//                bindProvider(OllamaSessionProvider(it.url))
                bindProvider(DeepseekSessionProvider(""))
            }
        }.launchIn(viewModelScope)
    }

    private fun bindProvider(newProvider: ChatSessionProvider) {
        providerScope.cancel()
        providerScope = CoroutineScope(Dispatchers.Default)
        provider = newProvider
        newProvider.modelsFlow().onEach {
            _modelsStateFlow.value = it
        }.launchIn(providerScope)
        newProvider.selectedModelFlow().onEach {
            _selectedModel.value = it
        }.launchIn(providerScope)
        providerScope.launch {
            provider?.listModels()
        }
    }

    fun selectModel(model: String) = viewModelScope.launch {
        if (model != _selectedModel.value) {
            generateContext = null
            _selectedModel.value = model
        }
    }

    fun sendMessage(value: String) = viewModelScope.launch {
        composingMessage.value?.let {
            _historyStateFlow.value += it
        }
        _composingMessage.value = null
        _historyStateFlow.value += ChatMessage(
            timestamp = System.currentTimeMillis(),
            text = value,
            role = Role.USER,
            completed = true
        )
        _composingMessage.value = ChatMessage(
            timestamp = System.currentTimeMillis(),
            text = "",
            role = Role.ASSISTANT,
            completed = false
        )
        provider?.chatWithModel(_historyStateFlow.value, value)?.collect {
            _composingMessage.value = _composingMessage.value?.copy(
                text = (_composingMessage.value?.text ?: "") + it
            )
        }
        _composingMessage.value = _composingMessage.value?.copy(completed = true)
    }

    override fun onCleared() {
        super.onCleared()
        providerScope.cancel()
    }
}