package top.ntutn.katbox

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
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
    }
}

actual fun getPlatform(): Platform = JVMPlatform()