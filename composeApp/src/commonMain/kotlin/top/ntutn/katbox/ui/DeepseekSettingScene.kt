package top.ntutn.katbox.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import top.ntutn.katbox.DeepseekSettingSceneVM
import top.ntutn.katbox.storage.ConnectionDataStore

@Composable
fun DeepseekSettingScene(dataStore: ConnectionDataStore, modifier: Modifier = Modifier, onCloseSetting: () -> Unit = {}) {
    val viewModel = viewModel { DeepseekSettingSceneVM(dataStore) }

    Column(modifier = modifier) {
        val url by viewModel.initialKey.collectAsState(Dispatchers.Main.immediate)
        val state = rememberTextFieldState(initialText = url)
        LaunchedEffect(url) {
            state.edit {
                replace(0, length, url)
            }
        }
        Row {
            Text("key")
            BasicSecureTextField(
                state = state,
                modifier = Modifier
                    .widthIn(min = 400.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
        }
        Row {
            Button(onClick = onCloseSetting) {
                Text("取消")
            }
            Button(onClick = {
                viewModel.saveData(state.text)
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