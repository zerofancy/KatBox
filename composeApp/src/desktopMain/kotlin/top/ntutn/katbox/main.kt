package top.ntutn.katbox

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
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

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    App.factory = Factory()
    application {
        LoggerFacade.init(LoggerFacade.VERBOSE)
        CrashAnalysisUtil.plant()
        MaterialTheme {
            KatWindowFrame(
                stringResource(Res.string.app_name),
                painterResource(Res.drawable.kat_box),
            )
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
