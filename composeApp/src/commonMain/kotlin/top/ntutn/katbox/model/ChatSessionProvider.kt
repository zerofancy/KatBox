package top.ntutn.katbox.model

import kotlinx.coroutines.flow.Flow

interface ChatSessionProvider {
    fun modelsFlow(): Flow<List<String>>

    fun selectedModelFlow(): Flow<String?>

    suspend fun listModels()

    suspend fun selectModel(modelName: String)

    suspend fun chatWithModel(prompt: String, content: String): Flow<String>
}