package top.ntutn.katbox.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay

/**
 * 该微件是对Markdown预览微件的一个特殊适配。
 * Markdown预览微件会在Markdown文本内容变化时闪烁，
 * 该微件通过延迟高度变化的方式避免用户滑动位置变化
 */
@Composable
fun StableMarkdownContainer(
    modifier: Modifier = Modifier,
    debounceTime: Long = 300,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    var measuredHeight by remember { mutableIntStateOf(-1) }
    var appliedHeight by remember { mutableStateOf(Dp.Unspecified) }
    var isInitialPass by remember { mutableStateOf(true) }

    LaunchedEffect(measuredHeight) {
        if (measuredHeight <= 0) return@LaunchedEffect

        val targetDp = with(density) { measuredHeight.toDp() }

        when {
            // 首次测量立即应用
            isInitialPass -> {
                appliedHeight = targetDp
                isInitialPass = false
            }
            // 后续变化防抖处理
            else -> {
                delay(debounceTime)
                appliedHeight = targetDp
            }
        }
    }

    Box(
        modifier = modifier
            .then(
                if (appliedHeight == Dp.Unspecified) {
                    Modifier.wrapContentHeight() // 首次测量不限制高度
                } else {
                    Modifier.heightIn(min = appliedHeight)
                }
            )
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .onSizeChanged { size ->
                    if (size.height > 0 && size.height != measuredHeight) {
                        measuredHeight = size.height
                    }
                }
        ) {
            content()
        }
    }
}
