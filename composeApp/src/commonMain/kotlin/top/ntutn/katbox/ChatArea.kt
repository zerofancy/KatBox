package top.ntutn.katbox

import androidx.compose.foundation.layout.Column
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

@Composable
fun ChatArea(modifier: Modifier = Modifier) {
    Column(modifier) {
        var history by remember { mutableStateOf("") }
        TextField(modifier = Modifier.fillMaxWidth().weight(1f), value = history, onValueChange = {})
        Row(modifier = Modifier.fillMaxWidth()) {
            var inputtingText by remember { mutableStateOf("") }
            TextField(value = inputtingText, modifier = Modifier.weight(1f), onValueChange = {
                inputtingText = it
            })
            Button(enabled = inputtingText.isNotBlank(), onClick = {
                history += inputtingText
                history += "\n"
                inputtingText = ""
            }) {
                Text("Send")
            }
        }
    }
}