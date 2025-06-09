package top.ntutn.katbox.ui

import KatBox.composeApp.BuildConfig
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.kat_box
import org.jetbrains.compose.resources.painterResource
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
    onOpenSetting: () -> Unit = {},
    onOpenAbout: () -> Unit = {},
) {
    Row(modifier) {
        var menuVisibility by remember { mutableStateOf(false) }
        if (menuVisibility) {
            Surface(shadowElevation = 4.dp) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    IconButton(onClick = { menuVisibility = false }) {
                        Icon(painterResource(Res.drawable.kat_box), contentDescription = "KatBox")
                    }
                    Button(onClick = onOpenSetting) {
                        Text("Setting")
                    }
                    Button(onClick = onOpenAbout) {
                        Text("About")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "version: " + BuildConfig.APP_VERSION)
                }
            }
        }
        Column {
            if (!menuVisibility) {
                Surface(shadowElevation = 4.dp) {
                    Row(Modifier.fillMaxWidth()) {
                        IconButton(onClick = { menuVisibility = true }) {
                            Icon(
                                painterResource(Res.drawable.kat_box),
                                contentDescription = "KatBox",
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            }

            val history by viewModel.historyStateFlow.collectAsState()
            val composing by viewModel.composingMessage.collectAsState()

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
}
