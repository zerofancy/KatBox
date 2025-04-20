package top.ntutn.katbox

import io.ktor.client.*
import kotlinx.serialization.json.Json

interface Platform {
    val name: String

    val httpClient: HttpClient

    val httpClientWithoutTimeout: HttpClient

    val jsonClient: Json

    fun initLogger()
}

expect fun getPlatform(): Platform