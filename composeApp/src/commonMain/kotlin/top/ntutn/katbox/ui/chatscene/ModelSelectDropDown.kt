package top.ntutn.katbox.ui.chatscene

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import top.ntutn.katbox.model.Model
import kotlin.collections.forEach

/**
 * 选择当前使用的大模型的选择框
 */
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