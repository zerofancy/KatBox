package top.ntutn.katbox

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowDecoration
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.app_name
import katbox.composeapp.generated.resources.kat_box
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun ApplicationScope.KatWindowFrame() {
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = stringResource(Res.string.app_name),
        icon = painterResource(Res.drawable.kat_box),
        decoration = WindowDecoration.Undecorated()
    ) {
        Surface(shadowElevation = 4.dp) {
            WindowDraggableArea {
                var currentTitlePointer by remember { mutableStateOf(PointerIcon.Default) }
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .pointerHoverIcon(currentTitlePointer)
                        .onDrag(enabled = true, onDrag = {}, onDragStart = {
                            currentTitlePointer = PointerIcon.Hand
                        }, onDragEnd = {
                            currentTitlePointer = PointerIcon.Default
                        }, onDragCancel = {
                            currentTitlePointer = PointerIcon.Default
                        })
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            painterResource(Res.drawable.kat_box),
                            contentDescription = stringResource(Res.string.app_name),
                            tint = Color.Unspecified
                        )
                    }
                    Text(stringResource(Res.string.app_name))
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = ::exitApplication) {
                        Text("X")
                    }
                }
            }
        }
    }
}