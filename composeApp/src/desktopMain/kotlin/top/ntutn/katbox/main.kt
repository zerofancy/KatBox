package top.ntutn.katbox

import KatBox.composeApp.BuildConfig
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowDecoration
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.viewModel
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.app_name
import katbox.composeapp.generated.resources.kat_box
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import top.ntutn.katbox.logger.LoggerFacade
import top.ntutn.katbox.storage.Factory
import top.ntutn.katbox.ui.ChatScene
import top.ntutn.katbox.ui.chatscene.ChatContentArea
import top.ntutn.katbox.ui.chatscene.InputtingArea

object App {
    lateinit var factory: Factory
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    App.factory = Factory()
    application {
        LoggerFacade.init(LoggerFacade.VERBOSE)
        CrashAnalysisUtil.plant()
        MaterialTheme {
            KatWindowFrame()
//        Window(
//            onCloseRequest = {
//                exitApplication()
//            },
//            title = stringResource(Res.string.app_name),
//            icon = painterResource(Res.drawable.kat_box),
//            decoration = WindowDecoration.Undecorated()
//        ) {
//                val factory = App.factory
//                val dataStore = remember(factory) { factory.createConnectionDataStore() }
//                val viewModel = viewModel { ChatAreaViewModel(dataStore) }
//
//                Row(Modifier) {
//                    var menuVisibility by remember { mutableStateOf(false) }
//                    if (menuVisibility) {
//                        Surface(shadowElevation = 4.dp) {
//                            Column(modifier = Modifier.fillMaxHeight()) {
//                                IconButton(onClick = { menuVisibility = false }) {
//                                    Icon(painterResource(Res.drawable.kat_box), contentDescription = "KatBox")
//                                }
//                                Button(onClick = {}) {
//                                    Text("Setting")
//                                }
//                                Button(onClick = {}) {
//                                    Text("About")
//                                }
//                                Spacer(modifier = Modifier.weight(1f))
//                                Text(text = "version: " + BuildConfig.APP_VERSION)
//                            }
//                        }
//                    }
//                    Column {
//                        if (!menuVisibility) {
//                            Surface(shadowElevation = 4.dp) {
//                                WindowDraggableArea {
//                                    Row(Modifier.fillMaxWidth()) {
//                                        IconButton(onClick = { menuVisibility = true }) {
//                                            Icon(
//                                                painterResource(Res.drawable.kat_box),
//                                                contentDescription = "KatBox",
//                                                tint = Color.Unspecified
//                                            )
//                                        }
//                                        Spacer(Modifier.weight(1f))
//                                        TextButton(onClick = ::exitApplication) {
//                                            Text("X")
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        val history by viewModel.historyStateFlow.collectAsState()
//                        val composing by viewModel.composingMessage.collectAsState()
//
//                        ChatContentArea(history, composing, Modifier.weight(1f))
//                        Surface(shadowElevation = 4.dp) {
//                            val models by viewModel.modelsStateFlow.collectAsState()
//                            val selectedModel by viewModel.selectedModel.collectAsState()
//                            InputtingArea(
//                                models,
//                                selectedModel,
//                                onSelectModel = viewModel::selectModel,
//                                onSendMessage = viewModel::sendMessage
//                            )
//                        }
//                    }
//                }
//            }
        }
    }
}
