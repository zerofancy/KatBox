package top.ntutn.katbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dorkbox.systemTray.MenuItem
import dorkbox.systemTray.SystemTray
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.kat_box
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KatBox",
        icon = painterResource(Res.drawable.kat_box)
    ) {
        App()
    }
    if (hostOs == OS.Windows) {
        ComposeTray(::exitApplication)
    } else {
        DorkTray(::exitApplication)
    }
}

@Composable
fun ApplicationScope.ComposeTray(onExit: () -> Unit = {}) {
    Tray(
        icon = painterResource(Res.drawable.kat_box),
        tooltip = "KatBox",
        onAction = onExit
    ) {
        Item("Exit", onClick = onExit) // not support chinese
    }
}

@Composable
fun DorkTray(onExit: () -> Unit = {}) {
    val tray by remember { mutableStateOf(SystemTray.get()!!) }

    LaunchedEffect(true) {
        //tray.installShutdownHook() // Auto-remove icon when application is killed
        tray.setImage(javaClass.getResourceAsStream("/kat-box.png")) // Use icon in src/main/resources/icon.png
        tray.menu.add(MenuItem("退出").apply {
            setCallback { onExit() }
        })
    }
}