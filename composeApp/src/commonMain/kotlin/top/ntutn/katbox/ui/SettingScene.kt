package top.ntutn.katbox.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import top.ntutn.katbox.SettingSceneVM

@Composable
fun SettingScene(modifier: Modifier = Modifier, onCloseSetting: () -> Unit = {}) {
    val viewModel = viewModel { SettingSceneVM() }

    Column(modifier = modifier) {
        Row {
            var connectionAddress by remember { mutableStateOf("") }
            Text("连接地址")
            TextField(value = connectionAddress, onValueChange = { connectionAddress = it })
        }
        Row {
            Button(onClick = onCloseSetting) {
                Text("取消")
            }
            Button(onClick = onCloseSetting) {
                Text("保存")
            }
        }
    }
}