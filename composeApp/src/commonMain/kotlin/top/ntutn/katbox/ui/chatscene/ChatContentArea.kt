package top.ntutn.katbox.ui.chatscene

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.ntutn.katbox.model.ChatMessage

@Composable
fun ChatContentArea(
    history: List<ChatMessage>,
    composing: ChatMessage?,
    modifier: Modifier = Modifier.Companion
) {
    val listState = rememberLazyListState()

    ScrollEndButtonContainer(listState = listState, modifier = modifier) {
        LazyColumn(
            modifier = Modifier.Companion.fillMaxSize(),
            state = listState
        ) {
            items(
                history.size,
                key = {
                    history[it].uuid
                }
            ) { i ->
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
    }
}