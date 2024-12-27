package dev.atrii.composewebkit.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

internal class Pull2RefreshState(
    val onRefresh: () -> Unit = { },
)

