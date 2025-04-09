package top.ntutn.katbox

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kdroid.composetray.tray.api.Tray
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.kat_box
import org.jetbrains.compose.resources.painterResource
import java.nio.file.Paths

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KatBox",
        icon = painterResource(Res.drawable.kat_box)
    ) {
        App()
    }
    val iconPath = Paths.get("src/desktopMain/resources/kat-box.png").toUri().toString()
    val windowsIconPath = Paths.get("src/desktopMain/resources/kat-box.ico").toUri().toString()
    Tray(
        iconPath = iconPath,
        windowsIconPath = windowsIconPath,
        tooltip = "My Application",
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
