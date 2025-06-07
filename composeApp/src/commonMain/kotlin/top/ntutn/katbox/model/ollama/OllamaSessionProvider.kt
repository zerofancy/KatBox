package top.ntutn.katbox.model.ollama

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import top.ntutn.katbox.getPlatform
import top.ntutn.katbox.logger.loggerFacade
import top.ntutn.katbox.model.ChatMessage
import top.ntutn.katbox.model.ChatSessionProvider

class OllamaSessionProvider(private val baseUrl: String): ChatSessionProvider {
    private val models = MutableStateFlow(listOf<Model>())
    private val selectedModel = MutableStateFlow<Model?>(null)
    private var generateContext: List<Int>? = null
    private val logger by loggerFacade("ollama_provider")

    override suspend fun listModels() {
        val url = runCatching {
            URLBuilder(baseUrl).apply {
                path("api/tags")
            }.buildString()
        }.getOrNull()
        val response = runCatching {
            require(url != null)
            getPlatform().httpClient.get(url)
        }.onFailure {
            logger.error{ log("fetch model failed", it) }
        }.getOrNull()
        models.value = response?.body<OllamaModelsResponse>()?.models ?: emptyList<Model>()
        selectedModel.value = models.value.firstOrNull()
    }

    override fun modelsFlow(): Flow<List<String>> {
        return models.map { it.map { it.name } }
    }

    override fun selectedModelFlow(): Flow<String?> {
        return selectedModel.map { it?.name }
    }

    override suspend fun selectModel(modelName: String) {
        selectedModel.value = models.value.find { it.name == modelName }
    }

    override suspend fun chatWithModel(
        history: List<ChatMessage>,
        content: String
    ): Flow<String> {
        val currentModelName = selectedModel.value?.name ?: return flow { }
        val models = models.value
        if (models.find { it.name == currentModelName } == null) {
            return flow { }
        }
        return generateResponseFlow(currentModelName, content)
    }

    private fun generateResponseFlow(selectedModel: String, value: String): Flow<String> = flow<String> {
        val request = GenerateRequest(
            model = selectedModel,
            prompt = value,
            stream = true,
            context = generateContext
        )
        val url = URLBuilder(baseUrl).apply {
            path("api/generate")
        }.buildString()
        val client = getPlatform().httpClientWithoutTimeout

        val channel = client.preparePost(url) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<ByteReadChannel>()

        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: break
            val response = withContext(Dispatchers.Default) { // 在后台线程解析
                getPlatform().jsonClient.decodeFromString<GenerateResponse>(line)
            }
            emit(response.response) // 发射每个片段
            response.context?.let { generateContext = it }
        }
    }.flowOn(Dispatchers.IO) // 确保网络请求在 IO 线程
        .catch { e ->
            // 可选：发射错误信息或记录日志
            throw e // 重新抛出以便在 collect 处处理
        }
}