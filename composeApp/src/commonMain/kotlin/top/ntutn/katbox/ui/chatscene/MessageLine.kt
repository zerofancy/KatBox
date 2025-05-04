package top.ntutn.katbox.ui.chatscene

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.ntutn.katbox.model.ChatMessage
import java.text.SimpleDateFormat

/**
 * 一条消息，可能是人类发送也可能是AI发送
 */
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

        if (showOriginMarkdown) {
            TextField(value = message.text, onValueChange = {}, modifier = Modifier.fillMaxWidth())
        } else {
            val result = remember(message.text) {
                val thinkStart = message.text.indexOf("<think>", ignoreCase = true)
                var thinkStop = -1
                var think = ""
                var output = ""
                if (thinkStart >= 0) {
                    thinkStop = message.text.indexOf("</think>", ignoreCase = true)
                    if (thinkStop < 0) {
                        thinkStop = message.text.length
                    } else {
                        thinkStop += 8
                    }
                    think = message.text.substring(thinkStart, thinkStop)
                    output = message.text.substring(thinkStop, message.text.length)
                } else {
                    output = message.text
                }
                think to output
            }

            if (result.first.isNotEmpty()) {
                var expanded by remember { mutableStateOf(true) }
                val outputEmpty = result.second.isEmpty()
                LaunchedEffect(outputEmpty) {
                    if (!outputEmpty) {
                        expanded = false
                    }
                }
                ThinkProcessViewer(
                    value = result.first, expanded = expanded,
                    modifier = Modifier.fillMaxWidth(),
                    onExpandChange = { expanded = !expanded })
            }

            // 避免上下跳动思路：延迟父布局高度变化
            StableMarkdownContainer {
                if (!message.completed) {
                    val index = result.second.indexOfLast {
                        it == '\n'
                    }
                    if (index in result.second.indices) {
                        val first = result.second.substring(0, index)
                        val second = result.second.substring(index, result.second.length)
                        Column {
                            // 避免闪烁思路：拆分出最后一个自然段，这样就算闪烁也只有最后一个段落
                            MarkdownViewer(first)
                            MarkdownViewer(second)
                        }
                    } else {
                        MarkdownViewer(result.second)
                    }
                } else {
                    MarkdownViewer(result.second)
                }
            }
        }
    }
}
