package dev.atrii.composewebkit

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
fun ComposeWebView(
    modifier: Modifier = Modifier,
    webViewState: ComposeWebViewState,
) {


    val context = LocalContext.current
    val initialUrl = rememberSaveable { webViewState.url }
    val webViewKey = rememberSaveable { "webview_$initialUrl".hashCode() }
    var webViewBundle by rememberSaveable { mutableStateOf<Bundle?>(null) }
    val webView = remember(webViewKey) {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewBundle?.let { bundle ->
                restoreState(bundle)
            } ?: loadUrl(initialUrl)
        }
    }

    var manager by remember {
        mutableStateOf<ComposeWebViewManager?>(null)
    }
    AndroidView(
        modifier = modifier,
        factory = { webView },
        update = { view ->
            manager = view.initWithManager(
                url = null,
            ) {
                webViewState.block(this)
            }
        }
    )

    BackHandler(webViewState.handleBackPressEvents) {
        manager ?: return@BackHandler
        webViewState.onBackPress(manager!!)
    }

    DisposableEffect(webViewKey) {
        onDispose {
            webViewBundle = Bundle().also { bundle ->
                webView.saveState(bundle)
            }
        }
    }

}

/**
 * State holder for ComposeWebView containing URL, back press handling, and configuration options.
 */
data class ComposeWebViewState(
    val url: String,
    val onBackPress: (ComposeWebViewManager) -> Unit,
    val handleBackPressEvents: Boolean = true,
    val block: ComposeWebViewManager.() -> Unit = {},
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
    configure: ComposeWebViewManager.() -> Unit = {},
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