package top.ntutn.katbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import top.ntutn.katbox.storage.Factory
import top.ntutn.katbox.ui.ChatScene
import top.ntutn.katbox.ui.SettingScene

@Composable
fun App(factory: Factory) {
    MaterialTheme {
        val dataStore = remember(factory) { factory.createConnectionDataStore() }
        val viewModel = viewModel { ChatAreaViewModel(dataStore) }
        var settingOpen by remember { mutableStateOf(false) }
        if (settingOpen) {
            SettingScene(dataStore) {
                settingOpen = false
            }
        } else {
            ChatScene(viewModel, Modifier.fillMaxSize(), onOpenSetting = {
                settingOpen = true
            })
        }
    }
}