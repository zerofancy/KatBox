package top.ntutn.katbox

import KatBox.composeApp.BuildConfig
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.viewModel
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.app_name
import katbox.composeapp.generated.resources.kat_box
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import top.ntutn.katbox.logger.LoggerFacade
import top.ntutn.katbox.logger.loggerFacade
import top.ntutn.katbox.storage.Factory
import top.ntutn.katbox.ui.DeepseekSettingScene
import top.ntutn.katbox.ui.OllamaSettingScene
import top.ntutn.katbox.ui.chatscene.ChatContentArea
import top.ntutn.katbox.ui.chatscene.InputtingArea

object App {
    lateinit var factory: Factory
}

private val logger by loggerFacade("main")

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    App.factory = Factory()
    application {
        LoggerFacade.init(LoggerFacade.VERBOSE)
        CrashAnalysisUtil.plant()
        MaterialTheme {
            val factory = App.factory
            var settingOpen by remember { mutableStateOf(false) }
            val dataStore = remember(factory) {
                logger.debug { log("create dataStore") }
                factory.createConnectionDataStore()
            }

            KatWindowFrame(
                stringResource(Res.string.app_name),
                painterResource(Res.drawable.kat_box),
                content = {
                    val viewModel = viewModel { ChatAreaViewModel(dataStore) }
                    val history by viewModel.historyStateFlow.collectAsState()
                    val composing by viewModel.composingMessage.collectAsState()

                    if (settingOpen) {
                        var currentSettingPosition by remember { mutableStateOf(SettingPosition.Selecting)}
                        LaunchedEffect(Unit) {
                            currentSettingPosition = SettingPosition.Selecting
                        }

                        when(currentSettingPosition) {
                            SettingPosition.Selecting -> SelectModelScreen(onSelectOllama = {
                                currentSettingPosition = SettingPosition.Ollama
                            }, onSelectDeepseek = {
                                currentSettingPosition = SettingPosition.Deepseek
                            })
                            SettingPosition.Ollama -> OllamaSettingScene(dataStore, onCloseSetting = {
                                settingOpen = false
                            })
                            SettingPosition.Deepseek -> DeepseekSettingScene(dataStore, onCloseSetting = {
                                settingOpen = false
                            })
                        }
                    } else {
                        Column {
                            ChatContentArea(history, composing, Modifier.weight(1f))
                            Surface(shadowElevation = 4.dp) {
                                val models by viewModel.modelsStateFlow.collectAsState()
                                val selectedModel by viewModel.selectedModel.collectAsState()
                                InputtingArea(
                                    models,
                                    selectedModel,
                                    onSelectModel = viewModel::selectModel,
                                    onSendMessage = viewModel::sendMessage
                                )
                            }
                        }
                    }
                },
                menuContent = {
                    Column {
                        Button(onClick = {
                            settingOpen = true
                        }) {
                            Text("Setting")
                        }
                        Button(onClick = {
                            getPlatform().openAbout()
                        }) {
                            Text("About")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "version: " + BuildConfig.APP_VERSION)
                    }
                }
            )
        }
    }
}
