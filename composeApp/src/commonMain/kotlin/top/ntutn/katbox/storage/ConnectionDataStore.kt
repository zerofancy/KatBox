package top.ntutn.katbox.storage

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import okio.FileSystem
import okio.Path.Companion.toPath

class ConnectionDataStore(private val produceFilePath: () -> String) {
    private val db = DataStoreFactory.create(storage = OkioStorage<ConnectionModel>(
        fileSystem = FileSystem.SYSTEM,
        serializer = ConnectionSerializer,
        producePath = {
            produceFilePath().toPath()
        }
    ))

    fun connectionData() = db.data

    suspend fun update(url: String) {
        db.updateData { prev ->
            prev.copy(url = url)
        }
    }
}