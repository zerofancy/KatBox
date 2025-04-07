package top.ntutn.katbox

import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.kat_box
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KatBox",
        icon = painterResource(Res.drawable.kat_box)
    ) {
        App()
    }
    Tray(
        icon = painterResource(Res.drawable.kat_box),
        tooltip = "KatBox",
        onAction = {
            exitApplication()
        }
    )
}