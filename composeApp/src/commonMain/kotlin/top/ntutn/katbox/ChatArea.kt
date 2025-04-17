package top.ntutn.katbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import top.ntutn.katbox.model.Model

@Composable
fun ChatArea(
    modifier: Modifier = Modifier,
    viewModel: ChatAreaViewModel = viewModel { ChatAreaViewModel() }
) {
    Column(modifier) {
        val history by viewModel.historyStateFlow.collectAsState()
        val composing by viewModel.composingMessage.collectAsState()
        val historyContent = remember(history, composing) {
            (history + composing).filterNotNull().joinToString(separator = "\n") {
                "${it.role}(${it.timestamp}): ${it.text}"
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth().weight(1f),
            value = historyContent,
            onValueChange = {})
        Row(modifier = Modifier.fillMaxWidth()) {
            var inputtingText by remember { mutableStateOf("") }
            TextField(value = inputtingText, modifier = Modifier.weight(1f), onValueChange = {
                inputtingText = it
            })

            val models by viewModel.modelsStateFlow.collectAsState()
            val selectedModel by viewModel.selectedModel.collectAsState()
            ModelSelectDropDown(models, selectedModel, onSelectModel = viewModel::selectModel)
            Button(enabled = inputtingText.isNotBlank(), onClick = {
                viewModel.sendMessage(inputtingText)
                inputtingText = ""
            }) {
                Text("Send")
            }
        }
        LaunchedEffect(Unit) {
            viewModel.fetchModels()
        }
    }
}

@Composable
fun ModelSelectDropDown(models: List<Model>, selectedModel: Model?, modifier: Modifier = Modifier, onSelectModel: (Model) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        TextButton(onClick = { expanded = true }) {
            Text(selectedModel?.name ?: "null")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = {
            expanded = false
        }, scrollState = scrollState) {
            models.forEach { model ->
                DropdownMenuItem(onClick = {
                    onSelectModel(model)
                    expanded = false
                }) {
                    Text(model.name)
                }
            }
        }
    }
}