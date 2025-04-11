package top.ntutn.katbox

import io.ktor.client.*

interface Platform {
    val name: String

    val httpClient: HttpClient
}

expect fun getPlatform(): Platform