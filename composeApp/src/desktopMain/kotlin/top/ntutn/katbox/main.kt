package top.ntutn.katbox

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kdroid.composetray.tray.api.Tray
import com.vaadin.open.Open
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.app_name
import katbox.composeapp.generated.resources.kat_box
import katbox.composeapp.generated.resources.tray_menu_about
import katbox.composeapp.generated.resources.tray_menu_exit
import katbox.composeapp.generated.resources.tray_menu_show_hide
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.skiko.hostOs

object App

fun main() = application {
    CrashAnalysisUtil.plant()
    var visible by remember { mutableStateOf(true) }
    Window(
        onCloseRequest = { visible = false},
        title = stringResource(Res.string.app_name),
        icon = painterResource(Res.drawable.kat_box),
        visible = visible,
    ) {
        App()
    }

    val iconPath = App.javaClass.classLoader!!.getResource("kat-box.png")!!.path
    val windowsIconPath = App.javaClass.classLoader!!.getResource("kat-box.ico")!!.path

    var trayResourceLoaded by remember { mutableStateOf(false) }
    var trayMenuVisibility by remember { mutableStateOf("") }
    var trayMenuAbout by remember { mutableStateOf("") }
    var trayMenuExit by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        trayMenuVisibility = getString(Res.string.tray_menu_show_hide)
        trayMenuAbout = getString(Res.string.tray_menu_about)
        trayMenuExit = getString(Res.string.tray_menu_exit)

        trayResourceLoaded = true
    }

    if (trayResourceLoaded) {
        Tray(
            iconPath = iconPath,
            windowsIconPath = windowsIconPath,
            tooltip = stringResource(Res.string.app_name),
            primaryAction = {
                if (!hostOs.isMacOS) {
                    visible = !visible
                }
            },
            primaryActionLinuxLabel = stringResource(Res.string.app_name)
        ) {
            if (hostOs.isWindows) {
                Item(label = trayMenuVisibility) {
                    visible = !visible
                }
                Divider()
            }
            Item(label = trayMenuAbout) {
                Open.open("https://github.com/zerofancy/KatBox")
            }
            Item(label = trayMenuExit, isEnabled = true) {
                dispose()
                exitApplication()
            }
        }
    }
}
