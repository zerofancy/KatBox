package top.ntutn.katbox.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import top.ntutn.katbox.SettingSceneVM
import top.ntutn.katbox.storage.ConnectionDataStore

@Composable
fun SettingScene(dataStore: ConnectionDataStore, modifier: Modifier = Modifier, onCloseSetting: () -> Unit = {}) {
    val viewModel = viewModel { SettingSceneVM(dataStore) }

    Column(modifier = modifier) {
        Row {
            Text("连接地址")
            val url by viewModel.inputtingUrl.collectAsState(Dispatchers.Main.immediate)
            TextField(value = url, onValueChange = viewModel::onInputChange)
        }
        Row {
            Button(onClick = onCloseSetting) {
                Text("取消")
            }
            Button(onClick = {
                viewModel.saveData()
                onCloseSetting()
            }) {
                Text("保存")
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.onInit()
    }
}