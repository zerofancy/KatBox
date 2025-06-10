package top.ntutn.katbox

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
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
fun ApplicationScope.KatWindowFrame(title: String, icon: Painter, content: @Composable () -> Unit = {}, menuContent: @Composable () -> Unit = {}) {
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = title,
        icon = icon,
        decoration = WindowDecoration.Undecorated()
    ) {
        var currentTitlePointer by remember { mutableStateOf(PointerIcon.Default) }
        var menuVisibility by remember { mutableStateOf(false) }
        if (menuVisibility) {
            Row {
                Column {
                    WindowDraggableArea {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .pointerHoverIcon(currentTitlePointer)
                                .onDrag(enabled = true, onDrag = {}, onDragStart = {
                                    currentTitlePointer = PointerIcon.Hand
                                }, onDragEnd = {
                                    currentTitlePointer = PointerIcon.Default
                                }, onDragCancel = {
                                    currentTitlePointer = PointerIcon.Default
                                }),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                menuVisibility = false
                            }) {
                                Icon(
                                    icon,
                                    contentDescription = title,
                                )
                            }
                            Text(title, overflow = TextOverflow.Ellipsis, maxLines = 1)
                        }
                    }
                    menuContent()
                }
                content()
            }
        } else {
            Column {
                Surface(shadowElevation = 4.dp) {
                    WindowDraggableArea {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .pointerHoverIcon(currentTitlePointer)
                                .onDrag(enabled = true, onDrag = {}, onDragStart = {
                                    currentTitlePointer = PointerIcon.Hand
                                }, onDragEnd = {
                                    currentTitlePointer = PointerIcon.Default
                                }, onDragCancel = {
                                    currentTitlePointer = PointerIcon.Default
                                }),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                menuVisibility = true
                            }) {
                                Icon(
                                    icon,
                                    contentDescription = title,
                                    tint = Color.Unspecified
                                )
                            }
                            Text(title, overflow = TextOverflow.Ellipsis, maxLines = 1)
                            Spacer(Modifier.weight(1f))
                            TextButton(onClick = ::exitApplication) {
                                Text("X")
                            }
                        }
                    }
                }
                content()
            }
        }
    }
}