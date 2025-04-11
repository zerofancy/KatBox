package top.ntutn.katbox

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val httpClient: HttpClient = HttpClient(CIO)
}

actual fun getPlatform(): Platform = JVMPlatform()