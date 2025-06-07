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

    suspend fun updateCurrentModel(modelType: ModelType) {
        db.updateData { prev ->
            prev.copy(type = modelType)
        }
    }

    suspend fun updateOllama(url: String) {
        db.updateData { prev ->
            val settingMap = prev.settingMap.toMutableMap()
            settingMap[ModelType.OLLAMA] = OllamaModelSetting(url = url)
            prev.copy(settingMap = settingMap)
        }
    }

    suspend fun updateDeepseek(key: String) {
        db.updateData { prev ->
            val settingMap = prev.settingMap.toMutableMap()
            settingMap[ModelType.DEEPSEEK] = DeepseekModelSetting(key)
            prev.copy(settingMap = settingMap)
        }
    }
}