package dev.atrii.composewebkit

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.atrii.composewebkit.interfaces.WebViewNavigator
import dev.atrii.composewebkit.interfaces.rememberWebViewNavigator
import dev.atrii.composewebkit.states.Pull2RefreshState
import kotlinx.coroutines.delay
import java.util.UUID

/**
 * A composable WebView wrapper that provides state management and configuration options.
 * Handles back navigation and state preservation across recomposition.
 */
@Composable
fun ComposeWebView(
    modifier: Modifier = Modifier,
    state: ComposeWebViewState,
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    pull2Refresh: Boolean = false,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = { },
    key: String = remember { state.url },
) {
    val context = LocalContext.current
    val viewModel = viewModel {
        ComposeWebViewModel(
            pull2Refresh = pull2Refresh,
            state = state,
        )
    }

    val instance = remember(key) { viewModel.getOrCreateInstance(key, context = context) }

    LaunchedEffect(true) {
        viewModel.setOnRefreshListener(key, onRefresh = onRefresh)
        viewModel.setNavigator(key, navigator = navigator)
        viewModel.setManager(key, block = state.block)
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing)
            viewModel.startRefreshing(key)
        else
            viewModel.stopRefreshing(key)
    }

    AndroidView(
        modifier = modifier,
        factory = {
            if (pull2Refresh) {
                instance.swipeRefreshLayout
            } else {
                instance.webView
            }
        },
        update = { _ -> }
    )

    BackHandler(state.handleBackPressEvents) {
        state.onBackPress(instance.manager)
    }
}

/**
 * State holder for ComposeWebView containing URL, back press handling, and configuration options.
 */
data class ComposeWebViewState(
    val url: String,
    val onBackPress: (ComposeWebViewManager) -> Unit,
    val handleBackPressEvents: Boolean = true,
    val block: ComposeWebViewManager.() -> Unit,
)

/**
 * Creates and remembers a ComposeWebViewState instance with the given configuration.
 */
@Composable
fun rememberComposeWebViewState(
    url: String,
    handleBackPressEvents: Boolean = true,
    onBackPress: (ComposeWebViewManager) -> Unit,
    key: Any? = null,
    configure: ComposeWebViewManager.() -> Unit,
): ComposeWebViewState {
    return remember(key) {
        ComposeWebViewState(
            url = url,
            handleBackPressEvents = handleBackPressEvents,
            onBackPress = onBackPress,
            block = configure
        )
    }
}