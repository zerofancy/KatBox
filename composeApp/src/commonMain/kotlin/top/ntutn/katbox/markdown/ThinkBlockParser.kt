package top.ntutn.katbox.markdown
import org.commonmark.parser.block.*
import org.commonmark.parser.SourceLine

class ThinkBlockParser : AbstractBlockParser() {
    private val block = ThinkBlock()
    private val content = StringBuilder()
    private var closed = false

    override fun getBlock() = block

    override fun tryContinue(state: ParserState): BlockContinue? {
        return if (closed) {
            BlockContinue.none()
        } else {
            // 持续捕获直到遇到闭合标签或文档结束
            BlockContinue.atIndex(state.index)
        }
    }

    override fun addLine(line: SourceLine) {
        // 注意：此时 line 已经跳过起始标签和缩进
        val lineStr = line.content

        // 检测闭合标签
        val endIndex = lineStr.indexOf("</think>", ignoreCase = true)
        if (endIndex != -1) {
            // 截取闭合标签前的内容
            content.append(lineStr.substring(0, endIndex))
            closed = true
        } else {
            // 追加整行内容（自动处理换行）
            content.append(lineStr)
        }

        // 保留原始换行结构
        content.append('\n')
    }

    override fun closeBlock() {
        // 最终处理：移除末尾多余换行并去空格
        block.content = content.toString()
            .trimEnd { it == '\n' }   // 保留内部换行
            .trimIndent()             // 移除公共缩进
    }

    // 3. 工厂类实现
    class Factory : AbstractBlockParserFactory() {
        override fun tryStart(
            state: ParserState,
            matchedBlockParser: MatchedBlockParser
        ): BlockStart? {
            if (state.indent >= 4) return null

            val line = state.line.content
            val nextNonSpace = state.nextNonSpaceIndex

            val startIndex = line.indexOf("<think>", nextNonSpace, ignoreCase = true)

            return when {
                startIndex == -1 -> null
                else -> {
                    val contentStart = startIndex + "<think>".length

                    BlockStart.of(ThinkBlockParser())
                        .atIndex(contentStart)
                }
            }
        }
    }
}
