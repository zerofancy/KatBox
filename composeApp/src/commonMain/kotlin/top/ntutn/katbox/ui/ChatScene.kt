package top.ntutn.katbox.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import top.ntutn.katbox.ChatAreaViewModel
import top.ntutn.katbox.ui.chatscene.ChatContentArea
import top.ntutn.katbox.ui.chatscene.InputtingArea

/**
 * 与大模型对话的主要对话页面
 */
@Composable
fun ChatScene(
    viewModel: ChatAreaViewModel,
    modifier: Modifier = Modifier,
    onOpenSetting: () -> Unit = {}
) {
    Column(modifier) {
        Surface(elevation = 4.dp) {
            Row(Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onOpenSetting) {
                    Text(stringResource(Res.string.app_name))
                }
            }
        }

        val history by viewModel.historyStateFlow.collectAsState()
        val composing by viewModel.composingMessage.collectAsState()

        ChatContentArea(history, composing, Modifier.weight(1f))
        Surface(elevation = 4.dp) {
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
