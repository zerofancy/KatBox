package top.ntutn.katbox.ui.chatscene

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * 选择当前使用的大模型的选择框
 */
@Composable
fun ModelSelectDropDown(
    models: List<String>,
    selectedModel: String?,
    modifier: Modifier = Modifier,
    onSelectModel: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        TextButton(onClick = { expanded = true }) {
            Text(selectedModel ?: "null")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = {
            expanded = false
        }, scrollState = scrollState) {
            models.forEach { model ->
                DropdownMenuItem(text = @Composable {
                    Text(model)
                }, onClick = {
                    onSelectModel(model)
                    expanded = false
                })
            }
        }
    }
}