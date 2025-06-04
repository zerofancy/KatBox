package top.ntutn.katbox.model.deepseek

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.oremif.deepseek.api.chat
import org.oremif.deepseek.api.models
import org.oremif.deepseek.client.DeepSeekClientStream
import org.oremif.deepseek.models.ModelInfo
import top.ntutn.katbox.model.ChatSessionProvider

class DeepseekSessionProvider(apiKey: String): ChatSessionProvider {
    private val client = DeepSeekClientStream(apiKey)
    private val modelsFlow = MutableStateFlow<List<ModelInfo>>(listOf())
    private val currentFlow = MutableStateFlow<ModelInfo?>(null)

    override fun modelsFlow(): Flow<List<String>> {
        return modelsFlow.map { it.map { it.id } }
    }

    override fun selectedModelFlow(): Flow<String?> {
        return currentFlow.map { it?.id }
    }

    override suspend fun listModels() {
        modelsFlow.value = client.models().data
        currentFlow.value = modelsFlow.value.firstOrNull()
    }

    override suspend fun selectModel(modelName: String) {
        currentFlow.value = modelsFlow.value.find { it.id == modelName }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun chatWithModel(
        prompt: String,
        content: String
    ): Flow<String> {
        return client.chat(content)
            .map {
                it.choices.joinToString("") { it.delta.content ?: "" }
            }
    }
}