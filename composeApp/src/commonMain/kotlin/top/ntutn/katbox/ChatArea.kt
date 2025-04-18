package top.ntutn.katbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Switch
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor
import top.ntutn.katbox.model.ChatMessage
import top.ntutn.katbox.model.Model
import java.text.SimpleDateFormat

@Composable
fun ChatArea(
    modifier: Modifier = Modifier,
    viewModel: ChatAreaViewModel = viewModel { ChatAreaViewModel() }
) {
    Column(modifier) {
        val history by viewModel.historyStateFlow.collectAsState()
        val composing by viewModel.composingMessage.collectAsState()
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
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
fun ModelSelectDropDown(
    models: List<Model>,
    selectedModel: Model?,
    modifier: Modifier = Modifier,
    onSelectModel: (Model) -> Unit
) {
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

@Composable
fun MessageLine(message: ChatMessage, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        var showOriginMarkdown by remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = message.role)
            Spacer(modifier = Modifier.width(8.dp))
            val sdf = remember { SimpleDateFormat("HH:mm:ss") }
            val timeString = remember(message.timestamp) {
                sdf.format(message.timestamp)
            }
            Text(text = timeString)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = if (showOriginMarkdown) "M" else "R")
            Switch(showOriginMarkdown, onCheckedChange = { showOriginMarkdown = it })
        }

        val state = rememberRichTextState()
        LaunchedEffect(message.text) {
            state.setMarkdown(message.text)
        }
        if (showOriginMarkdown) {
            TextField(value = message.text, onValueChange = {}, modifier = Modifier.fillMaxWidth())
        } else {
            OutlinedRichTextEditor(
                state = state,
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
        }
    }
}
