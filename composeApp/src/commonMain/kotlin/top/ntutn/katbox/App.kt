package top.ntutn.katbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.ntutn.katbox.ui.ChatArea
import top.ntutn.katbox.storage.Factory

@Composable
fun App(factory: Factory) {
    MaterialTheme {
        ChatArea(factory, Modifier.fillMaxSize())
    }
}