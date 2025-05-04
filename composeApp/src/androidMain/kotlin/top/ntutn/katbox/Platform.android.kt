package top.ntutn.katbox

import android.os.Build
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.serialization.json.Json

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val httpClient: HttpClient = HttpClient(OkHttp)
    override val httpClientWithoutTimeout: HttpClient
        get() = httpClient
    override val jsonClient: Json = Json

    override fun initLogger() {
        Napier.base(DebugAntilog())
    }

    override fun openAbout() {
        TODO("Not yet implemented")
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()