package top.ntutn.katbox

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.app_name
import katbox.composeapp.generated.resources.kat_box
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import top.ntutn.katbox.logger.LoggerFacade
import top.ntutn.katbox.storage.Factory

object App {
    lateinit var factory: Factory
}

fun main() {
    App.factory = Factory()
    application {
        LoggerFacade.init(LoggerFacade.VERBOSE)
        CrashAnalysisUtil.plant()
        Window(
            onCloseRequest = {
                exitApplication()
            },
            title = stringResource(Res.string.app_name),
            icon = painterResource(Res.drawable.kat_box),
        ) {
            App(App.factory)
        }
    }
}
