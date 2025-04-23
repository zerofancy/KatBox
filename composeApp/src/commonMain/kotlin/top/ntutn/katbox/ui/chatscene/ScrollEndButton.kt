package top.ntutn.katbox.ui.chatscene

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

/**
 * 附带一个“滚动到最后”按钮的容器
 */
@Composable
fun ScrollEndButtonContainer(listState: LazyListState, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        val scope = rememberCoroutineScope() // 获取协程作用域

        var autoScrollEnabled by remember { mutableStateOf(false) }

        LaunchedEffect(listState.isScrollInProgress) {
            if (listState.isScrollInProgress) {
                // 检测到用户手动滚动时关闭自动模式
                autoScrollEnabled = false
            }
        }

        LaunchedEffect(autoScrollEnabled, listState.canScrollForward) {
            if (autoScrollEnabled && listState.canScrollForward) {
                listState.scrollToBottomEnd()
            }
        }

        content()

        // 显示滚动到底部按钮的条件
        if (listState.canScrollForward) {
            OutlinedButton(
                onClick = {
                    scope.launch {
                        listState.scrollToBottomEnd()
                        autoScrollEnabled = true
                    }
                },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Text("\uD83D\uDC47")
            }
        }
    }
}

private suspend fun LazyListState.scrollToBottomEnd() {
    while (true) {
        if (scrollBy(10f) < 0.01f) {
            break
        }
    }
}
