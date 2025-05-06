package top.ntutn.katbox.ui.chatscene

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.chat_area_send_button
import org.jetbrains.compose.resources.stringResource
import top.ntutn.katbox.model.ollama.Model

@Composable
fun InputtingArea(
    models: List<String>,
    selectedModel: String?,
    modifier: Modifier = Modifier.Companion,
    onSelectModel: (String) -> Unit = {},
    onSendMessage: (String) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        var inputtingText by remember { mutableStateOf("") }
        TextField(value = inputtingText, modifier = Modifier.Companion.weight(1f), onValueChange = {
            inputtingText = it
        })

        ModelSelectDropDown(models, selectedModel, onSelectModel = onSelectModel)
        Button(enabled = inputtingText.isNotBlank(), onClick = {
            onSendMessage(inputtingText)
            inputtingText = ""
        }) {
            Text(text = stringResource(Res.string.chat_area_send_button))
        }
    }
}