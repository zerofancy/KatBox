package top.ntutn.katbox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import katbox.composeapp.generated.resources.Res
import katbox.composeapp.generated.resources.chat_aren_think_progress
import org.jetbrains.compose.resources.stringResource

/**
 * 显示支持推理的大模型的思考过程
 */
@Composable
fun ThinkProcessViewer(value: String, expanded: Boolean, modifier: Modifier = Modifier, onExpandChange: () -> Unit) {
    Column(modifier = modifier) {
        TextButton(onClick = onExpandChange) {
            Row {
                Text(text = stringResource(Res.string.chat_aren_think_progress))
                if (expanded) {
                    Text("▼")
                } else {
                    Text("▶")
                }
            }
        }
        if (expanded) {
            SelectionContainer {
                Text(value, modifier = Modifier.background(Color.LightGray))
            }
        }
    }
}