package top.ntutn.katbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import top.ntutn.katbox.storage.Factory
import top.ntutn.katbox.ui.ChatScene
import top.ntutn.katbox.ui.DeepseekSettingScene
import top.ntutn.katbox.ui.OllamaSettingScene

@Composable
fun App(factory: Factory) {
    MaterialTheme {
        val dataStore = remember(factory) { factory.createConnectionDataStore() }
        val viewModel = viewModel { ChatAreaViewModel(dataStore) }
        var settingOpen by remember { mutableStateOf(false) }
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
            ChatScene(viewModel, Modifier.fillMaxSize(), onOpenSetting = {
                settingOpen = true
            }, onOpenAbout = {
                getPlatform().openAbout()
            })
        }
    }
}

enum class SettingPosition {
    Selecting,
    Ollama,
    Deepseek,
}

@Composable
fun SelectModelScreen(modifier: Modifier = Modifier, onSelectOllama: () -> Unit = {}, onSelectDeepseek: () -> Unit = {}) {
    Column {
        Button(onClick = onSelectOllama) {
            Text("Ollama")
        }
        Button(onClick = onSelectDeepseek) {
            Text("Deepseek")
        }
    }
}
