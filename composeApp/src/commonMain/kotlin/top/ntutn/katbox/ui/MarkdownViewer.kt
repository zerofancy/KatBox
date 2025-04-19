package top.ntutn.katbox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.commonmark.node.BulletList
import org.commonmark.node.Document
import org.commonmark.node.Heading
import org.commonmark.node.HtmlBlock
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.Paragraph
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text
import org.commonmark.node.ThematicBreak
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import top.ntutn.katbox.logger.slf4jLogger
import top.ntutn.katbox.markdown.ThinkBlock
import top.ntutn.katbox.markdown.ThinkBlockParser

private val logger by slf4jLogger("markdown")

@Composable
fun MarkdownViewer(text: String, modifier: Modifier = Modifier) {

    var document by remember { mutableStateOf<Node?>(null) }

    // 使用独立的协程作用域控制解析任务
    val parseScope = rememberCoroutineScope { Dispatchers.Default }

    LaunchedEffect(text) {
        parseScope.coroutineContext.cancelChildren()
        parseScope.launch {
            val snapshotText = text
            val parser: Parser = Parser
                .builder()
                .customBlockParserFactory(ThinkBlockParser.Factory())
                .build()
            val parsed = parser.parse(snapshotText)
            document = parsed
        }
    }

    Column(modifier = modifier) {
        SelectionContainer {
            document?.let { MarkdownNode(it) }
        }
    }
    Button(onClick = {
        val renderer = HtmlRenderer.builder().build()
        val html = renderer.render(document)
        logger.info(html)
    }) {
        androidx.compose.material.Text("Print html")
    }
}

@Composable
fun MarkdownNode(node: Node, modifier: Modifier = Modifier) {
    val buildChildren: @Composable () -> Unit = {
        // build children elements
        var child = node.firstChild
        while (child != null) {
            MarkdownNode(child, modifier)
            child = child.next
        }
    }

    when(node) {
        is Document -> DocumentNode(node, content = buildChildren)
        is HtmlBlock -> HtmlBlockNode(node, content = buildChildren) // fixme 要特殊处理
        is Paragraph -> ParagraphNode(node, content = buildChildren) // p
        is StrongEmphasis -> StrongEmphasisNode(node, content = buildChildren) // 加粗
        is ThinkBlock -> ThinkBlockNode(node) // 思考过程
        is Text -> TextNode(node) // 文本内容
        is ThematicBreak -> ThematicBreakNode(node) // 分割线
        is Heading -> HeadingNode(node, content = buildChildren) // 标题
        is BulletList -> BulletListNode(node, content = buildChildren) // 列表
        is ListItem -> ListItemNode(node, content = buildChildren)
        else -> {
            LaunchedEffect(node) {
                logger.warn("Unknown node {}", node)
            }
        }
    }
}

@Composable
private fun ListItemNode(node: ListItem, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    content()
}

@Composable
private fun BulletListNode(node: BulletList, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Row(modifier = modifier) {
        androidx.compose.material.Text(node.marker)
        content()
    }
}

@Composable
private fun HeadingNode(node: Heading, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalTextStyle provides TextStyle(fontWeight = FontWeight.Bold)
    ) {
        content()
    }
}

@Composable
private fun ThematicBreakNode(node: ThematicBreak, modifier: Modifier = Modifier) {
    Divider(modifier = modifier, thickness = node.literal?.length?.dp ?: 3.dp)
}

@Composable
private fun StrongEmphasisNode(node: StrongEmphasis, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalTextStyle provides TextStyle(fontWeight = FontWeight.Bold)
    ) {
        content()
    }
}

@Composable
private fun ThinkBlockNode(node: ThinkBlock, modifier: Modifier = Modifier) {
    androidx.compose.material.Text(text = node.content, modifier.background(Color.LightGray))
}

// fixme 需要保证显示一行后自动换行
@Composable
private fun TextNode(node: Text, modifier: Modifier = Modifier) {
    androidx.compose.material.Text(text = node.literal, modifier)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DocumentNode(node: Document, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier) {
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HtmlBlockNode(node: HtmlBlock, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    node.literal?.takeIf { it.isNotEmpty() }?.let {
        androidx.compose.material.Text(text = node.literal, Modifier)
    }
    Column(modifier) {
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ParagraphNode(node: Paragraph, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier) {
        content()
    }
}