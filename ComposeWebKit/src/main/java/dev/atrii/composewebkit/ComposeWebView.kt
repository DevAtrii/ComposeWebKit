package dev.atrii.composewebkit

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView


/**
 * A composable WebView wrapper that provides state management and configuration options.
 * Handles back navigation and state preservation across recomposition.
 */
@Composable
@Stable
fun ComposeWebView(
    modifier: Modifier = Modifier,
    state: ComposeWebViewState,
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    pull2Refresh: Boolean = false,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = { },
    key: String = rememberSaveable { state.url },
) {

    val context = LocalContext.current
    var webViewBundle by rememberSaveable { mutableStateOf<Bundle?>(null) }

    val webViewState = remember(key) {
        ComposeWebViewInstancesManager(
            state = state,
            pull2Refresh = pull2Refresh,
            webViewBundle = webViewBundle
        )
    }

    val instance = remember(key) { webViewState.getOrCreateInstance(key, context = context) }

    LaunchedEffect(true) {
        webViewState.setOnRefreshListener(key, onRefresh)
        webViewState.setNavigator(key, navigator)
        webViewState.setManager(key, state.block)
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing)
            webViewState.startRefreshing(key)
        else
            webViewState.stopRefreshing(key)
    }

    DisposableEffect(key) {
        onDispose {
            webViewBundle = Bundle().also { bundle ->
                instance.webView.saveState(bundle)
            }
        }
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
@Stable
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
@Stable
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