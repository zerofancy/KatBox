package top.ntutn.katbox.storage

import androidx.datastore.core.okio.OkioSerializer
import okio.BufferedSink
import okio.BufferedSource
import top.ntutn.katbox.getPlatform

object ConnectionSerializer: OkioSerializer<ConnectionModel> {
    override val defaultValue: ConnectionModel = ConnectionModel("http://localhost:11434", 0)

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