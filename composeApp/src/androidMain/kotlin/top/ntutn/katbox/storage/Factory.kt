package top.ntutn.katbox.storage

import android.app.Application

actual class Factory(private val app: Application) {
    actual fun createConnectionDataStore(): ConnectionDataStore {
        return ConnectionDataStore {
            app.filesDir.resolve("connection.json").absolutePath
        }
    }
}