package top.ntutn.katbox.storage

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.Serializable
import okio.BufferedSink
import okio.BufferedSource
import top.ntutn.katbox.getPlatform

@Serializable
enum class ModelType {
    OLLAMA,
    DEEPSEEK
}

@Serializable
sealed class ModelSetting

@Serializable
class OllamaModelSetting(val url: String) : ModelSetting()

@Serializable
class DeepseekModelSetting(val key: String) : ModelSetting()

@Serializable
data class ConnectionModel(val type: ModelType, val settingMap: Map<ModelType, ModelSetting>)

object ConnectionSerializer: OkioSerializer<ConnectionModel> {
    override val defaultValue: ConnectionModel = ConnectionModel(type = ModelType.OLLAMA, settingMap = mapOf())

    override suspend fun readFrom(source: BufferedSource): ConnectionModel {
        return getPlatform().jsonClient.decodeFromString(source.readUtf8())
    }

    override suspend fun writeTo(
        t: ConnectionModel,
        sink: BufferedSink
    ) {
        sink.use {
            it.writeUtf8(getPlatform().jsonClient.encodeToString(ConnectionModel.serializer(), t))
        }
    }
}