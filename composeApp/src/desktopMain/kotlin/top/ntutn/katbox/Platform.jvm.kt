package top.ntutn.katbox

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class JVMPlatform: Platform {
    override val jsonClient = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true  // 自动转换无效值为默认值
        explicitNulls = false     // 不序列化null值字段
    }

    override val name: String = "Java ${System.getProperty("java.version")}"
    override val httpClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(jsonClient)
        }
    }
    override val httpClientWithoutTimeout: HttpClient = HttpClient(CIO) {
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