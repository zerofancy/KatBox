package top.ntutn.katbox

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class JVMPlatform: Platform {
    private val jsonClient = Json {
        ignoreUnknownKeys = true
    }

    override val name: String = "Java ${System.getProperty("java.version")}"
    override val httpClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(jsonClient)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = Long.MAX_VALUE
            socketTimeoutMillis = Long.MAX_VALUE
            connectTimeoutMillis = Long.MAX_VALUE
        }
    }
}

actual fun getPlatform(): Platform = JVMPlatform()