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
import top.ntutn.katbox.ui.MarkdownNode
import top.ntutn.katbox.ui.MarkdownViewer
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
//            OutlinedRichTextEditor(
//                state = state,
//                modifier = Modifier.fillMaxWidth(),
//                readOnly = true
//            )
            val debugContent = """
                <think>

                嗯，用户问“草为什么是绿的”，这个问题看起来简单，但其实涉及植物学和光学的知识。首先，我需要回忆一下植物的光合作用过程，因为颜色通常和光的吸收或反射有关。草是绿色的，可能是因为它反射了绿色的光，而吸收了其他颜色的光用于光合作用。

                不过，具体来说，叶绿素是关键。叶绿素是植物中的主要色素，负责吸收光能。我记得叶绿素主要吸收红光和蓝光，而绿光被反射或透射，所以看起来是绿色的。但为什么植物选择吸收红光和蓝光而不是其他颜色呢？可能因为这些波长的光在光谱中有较高的能量或者更有效用于光合作用的反应。

                另外，可能还有其他色素，比如类胡萝卜素，它们吸收蓝光，但在绿色植物中叶绿素占主导，所以整体颜色以绿色为主。不过，用户可能还会想知道为什么不是其他颜色，比如为什么不是红色或蓝色？可能因为叶绿素的结构决定了它对特定波长的吸收，而进化上可能因为地球上的阳光成分，红光和蓝光更充足，所以植物适应了这种环境。

                还有，可能用户对光的反射和吸收机制不太清楚，需要解释光如何被吸收和反射的关系。比如，当物体看起来是某种颜色时，是因为它反射了那种颜色的光，而吸收了其他颜色。所以草反射绿光，所以是绿色的。

                不过，可能还有其他因素，比如不同季节颜色变化，比如秋天叶子变黄，这是因为叶绿素分解，露出其他色素的颜色。但这里的问题是关于草，通常草在生长季节保持绿色，所以可能不需要涉及季节变化，但可以稍微提一下。

                另外，可能用户想知道更深入的机制，比如叶绿素的化学结构如何影响其吸收光谱。叶绿素a和叶绿素b的结构不同，吸收峰的位置也不同，但大致在红光和蓝光区域。而绿光的波长（约500-570纳米）可能因为被较少吸收而被反射，所以看起来绿。

                不过，有没有其他可能性？比如，有没有其他因素导致草呈现绿色？比如，是否可能因为进化上的优势，比如绿色在环境中更隐蔽？或者是否因为光的可用性？比如在水下植物可能颜色不同，但陆地植物主要吸收红光和蓝光，反射绿光。

                另外，可能需要澄清，绿色其实是人类感知的颜色，其他动物可能看到不同的颜色，但问题是从人类视觉出发，所以不需要深入讨论其他生物的视觉。

                总结一下，回答的要点应该是：叶绿素吸收红光和蓝光，反射绿光，因此草呈现绿色。同时可以补充叶绿素的作用，光合作用，以及可能的进化原因。需要确保用简单易懂的语言，避免过于专业的术语，但也要准确。

                需要检查是否有常见的误解，比如有人可能认为植物是绿色是因为它们需要绿色光，但实际上它们反射绿光，所以可能需要澄清这一点。另外，是否所有植物都是绿色的？比如有些藻类可能颜色不同，但常见陆地植物主要是绿色，因为叶绿素占主导。

                最后，可能用户是孩子或刚开始学科学的人，所以需要解释得清晰，步骤分明，可能分点回答会更好。
                </think>

                草之所以呈现绿色，主要与植物中的一种关键色素——**叶绿素**有关。以下是详细解释：

                ---

                ### 1. **叶绿素的作用**
                   - **光合作用**：叶绿素是植物进行光合作用的核心分子，它能吸收太阳光中的能量，将二氧化碳和水转化为葡萄糖（能量）和氧气。
                   - **吸收与反射**：叶绿素主要吸收**红光（640-680纳米）和蓝光（430-450纳米）**，而对**绿光（约500-570纳米）**的吸收较弱。因此，绿光被反射或透射出来，导致我们看到的草是绿色的。

                ---

                ### 2. **为什么选择吸收红光和蓝光？**
                   - **能量效率**：红光和蓝光的能量适合驱动光合作用中的化学反应。红光波长较长，穿透力强，能深入叶片内部；蓝光波长较短，适合表面吸收。
                   - **太阳光谱**：地球大气层对太阳光的散射和吸收后，红光和蓝光在到达地面时仍具有较高能量，因此植物进化出适应这些波长的色素。

                ---

                ### 3. **其他色素的辅助作用**
                   - **类胡萝卜素**：除叶绿素外，植物还含有黄色或橙色的类胡萝卜素，它们主要吸收蓝光。但在大多数绿色植物中，叶绿素的含量远高于其他色素，因此主导了叶片的绿色。
                   - **季节变化**：在秋季，叶绿素分解后，类胡萝卜素的颜色会显现（如叶子变黄），但草通常在生长季节保持绿色。

                ---

                ### 4. **为什么不是其他颜色？**
                   - 如果植物吸收绿光，可能无法有效利用光能，因为绿光的能量介于红光和蓝光之间，效率较低。
                   - 进化过程中，叶绿素的结构（如镁离子和卟啉环）使其对红光和蓝光的吸收最优化，因此绿色成为陆地植物的普遍选择。

                ---

                ### 5. **颜色感知的原理**
                   - 当阳光照射到叶片时，叶绿素吸收红光和蓝光，而绿光被反射或透射。反射的绿光进入人眼，大脑将其解读为“绿色”。

                ---

                ### 总结
                草是绿色的，是因为叶绿素在光合作用中优先吸收红光和蓝光，而将绿光反射出来。这种特性是植物长期适应地球光照环境和光合作用效率的结果。

                如果想进一步了解，可以探讨叶绿素的化学结构、其他植物色素（如花青素）的作用，或不同星球上可能存在的植物颜色差异。
            """.trimIndent()
            MarkdownViewer(debugContent, Modifier.fillMaxWidth())
        }
    }
}
