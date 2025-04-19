package top.ntutn.katbox

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownCheckBox
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.compose.extendedspans.SquigglyUnderlineSpanPainter
import com.mikepenz.markdown.compose.extendedspans.rememberSquigglyUnderlineAnimator
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.markdownExtendedSpans
import com.mikepenz.markdown.model.rememberMarkdownState
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
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
        var showOriginMarkdown by remember { mutableStateOf(!message.completed) }
        // todo Markdown刷新时会闪烁，现在默认不渲染在完成时一次性渲染避免闪烁
        LaunchedEffect(message.completed) {
            if (message.completed) {
                showOriginMarkdown = false
            }
        }
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
                        thinkStop = message.text.length - 1
                    } else {
                        thinkStop += 8
                    }
                    think = message.text.substring(thinkStart, thinkStop)
                    if (thinkStop < message.text.length - 1) {
                        output = message.text.substring(thinkStop, message.text.length - 1)
                    }
                } else {
                    output = message.text
                }
                think to output
            }

            if (result.first.isNotEmpty()) {
                SelectionContainer {
                    Text(result.first, modifier = Modifier.background(Color.LightGray))
                }
            }

            MarkdownViewer(result.second)
        }
    }
}

@Composable
fun MarkdownViewer(markdown: String, modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val highlightsBuilder = remember(isDarkTheme) {
        Highlights.Builder().theme(SyntaxThemes.atom(darkMode = isDarkTheme))
    }
    SelectionContainer {
        Markdown(
            markdownState = rememberMarkdownState(markdown),
            colors = markdownColor(),
            typography = markdownTypography(),
            components = markdownComponents(
                codeBlock = {
                    MarkdownHighlightedCodeBlock(
                        content = it.content,
                        node = it.node,
                        highlights = highlightsBuilder
                    )
                },
                codeFence = {
                    MarkdownHighlightedCodeFence(
                        content = it.content,
                        node = it.node,
                        highlights = highlightsBuilder
                    )
                },
                checkbox = { MarkdownCheckBox(it.content, it.node, it.typography.text) }
            ),
            extendedSpans = markdownExtendedSpans {
                val animator = rememberSquigglyUnderlineAnimator()
                remember {
                    ExtendedSpans(
                        RoundedCornerSpanPainter(),
                        SquigglyUnderlineSpanPainter(animator = animator)
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(16.dp)
        )
    }
}

@Composable
fun markdownColor(
    text: Color = MaterialTheme.colors.onBackground,
    codeBackground: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
    inlineCodeBackground: Color = codeBackground,
    dividerColor: Color = MaterialTheme.colors.secondaryVariant,
    tableBackground: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.02f),
): MarkdownColors = DefaultMarkdownColors(
    text = text,
    codeText = Color.Unspecified,
    inlineCodeText = Color.Unspecified,
    linkText = Color.Unspecified,
    codeBackground = codeBackground,
    inlineCodeBackground = inlineCodeBackground,
    dividerColor = dividerColor,
    tableText = Color.Unspecified,
    tableBackground = tableBackground,
)


@Composable
fun markdownTypography(
    h1: TextStyle = MaterialTheme.typography.h1,
    h2: TextStyle = MaterialTheme.typography.h2,
    h3: TextStyle = MaterialTheme.typography.h3,
    h4: TextStyle = MaterialTheme.typography.h4,
    h5: TextStyle = MaterialTheme.typography.h5,
    h6: TextStyle = MaterialTheme.typography.h6,
    text: TextStyle = MaterialTheme.typography.body1,
    code: TextStyle = MaterialTheme.typography.body1.copy(fontFamily = FontFamily.Monospace),
    inlineCode: TextStyle = text.copy(fontFamily = FontFamily.Monospace),
    quote: TextStyle = MaterialTheme.typography.body2.plus(SpanStyle(fontStyle = FontStyle.Italic)),
    paragraph: TextStyle = MaterialTheme.typography.body1,
    ordered: TextStyle = MaterialTheme.typography.body1,
    bullet: TextStyle = MaterialTheme.typography.body1,
    list: TextStyle = MaterialTheme.typography.body1,
    link: TextStyle = MaterialTheme.typography.body1.copy(
        fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline
    ),
    textLink: TextLinkStyles = TextLinkStyles(style = link.toSpanStyle()),
    table: TextStyle = text,
): MarkdownTypography = DefaultMarkdownTypography(
    h1 = h1,
    h2 = h2,
    h3 = h3,
    h4 = h4,
    h5 = h5,
    h6 = h6,
    text = text,
    quote = quote,
    code = code,
    inlineCode = inlineCode,
    paragraph = paragraph,
    ordered = ordered,
    bullet = bullet,
    list = list,
    link = link,
    textLink = textLink,
    table = table,
)
