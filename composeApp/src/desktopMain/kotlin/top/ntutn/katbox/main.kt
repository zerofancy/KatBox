package top.ntutn.katbox

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kdroid.composetray.tray.api.Tray
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.app_name
import katbox.composeapp.generated.resources.kat_box
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object App

fun main() = application {
    CrashAnalysisUtil.plant()
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        icon = painterResource(Res.drawable.kat_box)
    ) {
        App()
    }

    val iconPath = App.javaClass.classLoader!!.getResource("kat-box.png")!!.path
    val windowsIconPath = App.javaClass.classLoader!!.getResource("kat-box.ico")!!.path
    Tray(
        iconPath = iconPath,
        windowsIconPath = windowsIconPath,
        tooltip = stringResource(Res.string.app_name),
        primaryAction = {
        },
        primaryActionLinuxLabel = "Open Application"
    ) {
        SubMenu(label = "Options") {
            Item(label = "Setting 1") {
            }
            SubMenu(label = "Advanced Sub-options") {
                Item(label = "Advanced Option 1") {
                }
                Item(label = "Advanced Option 2") {
                }
            }
        }

        Divider()

        SubMenu(label = "Tools") {
            Item(label = "Calculator") {
            }
            Item(label = "Notepad") {
            }
        }

        Divider()

        CheckableItem(label = "Enable notifications") { isChecked ->
        }

        Divider()

        Item(label = "About") {
        }

        Divider()

        Item(label = "Exit", isEnabled = true) {
            dispose()
            exitApplication()
        }

        Item(label = "Version 1.0.0", isEnabled = false)
    }
}
