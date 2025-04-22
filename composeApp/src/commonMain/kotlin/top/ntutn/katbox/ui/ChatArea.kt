package top.ntutn.katbox.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.chat_area_send_button
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import top.ntutn.katbox.ChatAreaViewModel
import top.ntutn.katbox.storage.ConnectionDataStore
import top.ntutn.katbox.storage.Factory

/**
 * 与大模型对话的主要对话页面
 */
@Composable
fun ChatArea(
    factory: Factory,
    modifier: Modifier = Modifier,
    viewModel: ChatAreaViewModel = viewModel { ChatAreaViewModel() }
) {
    Column(modifier) {
        val history by viewModel.historyStateFlow.collectAsState()
        val composing by viewModel.composingMessage.collectAsState()

        val listState = rememberLazyListState()

        ScrollEndButtonContainer(listState = listState, modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                items(history.size) { i ->
                    val item = history[i]
                    MessageLine(message = item)
                }
                val item = composing
                if (item != null) {
                    item {
                        MessageLine(item)
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            var inputtingText by remember { mutableStateOf("") }
            TextField(value = inputtingText, modifier = Modifier.weight(1f), onValueChange = {
                inputtingText = it
            })

            val models by viewModel.modelsStateFlow.collectAsState()
            val selectedModel by viewModel.selectedModel.collectAsState()
            ModelSelectDropDown(models, selectedModel, onSelectModel = viewModel::selectModel)
            val scope = rememberCoroutineScope()
            Button(enabled = inputtingText.isNotBlank(), onClick = {
                viewModel.sendMessage(inputtingText)
                inputtingText = ""
                val dataStore: ConnectionDataStore = factory.createConnectionDataStore()
                scope.launch {
                    dataStore.update()
                }

            }) {
                Text(text = stringResource(Res.string.chat_area_send_button))
            }
        }
        LaunchedEffect(Unit) {
            viewModel.fetchModels()
        }
    }
}
