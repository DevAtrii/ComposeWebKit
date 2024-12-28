package dev.atrii.composewebkit

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.runtime.Stable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Stable
class ComposeWebViewInstancesManager(
    private val state: ComposeWebViewState,
    private val webViewBundle: Bundle?,
    private val pull2Refresh: Boolean = false,
) {

    // Create a data class to hold the view instances
    data class WebViewContainer(
        val webView: WebView,
        val swipeRefreshLayout: SwipeRefreshLayout,
        var manager: ComposeWebViewManager,
    )

    // Use a map to store multiple instances
    private val viewInstances = mutableMapOf<String, WebViewContainer>()

    // Factory method to create or get existing instance
    fun getOrCreateInstance(key: String, context: Context): WebViewContainer {
        return viewInstances.getOrPut(key) {
            var isStateRestored = false
            val webView = WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewBundle?.let { bundle ->
                    restoreState(bundle)
                    isStateRestored = true
                }
            }

            // Create new SwipeRefreshLayout
            val swipeRefreshLayout = SwipeRefreshLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                if (pull2Refresh) {
                    addView(webView)
                }
            }

            // Create new manager
            val manager = ComposeWebViewManager(
                url = null,
                webView = webView,
                block = state.block
            )

            // Load initial URL
            if (!isStateRestored) {

                webView.loadUrl(state.url)
            }

            WebViewContainer(webView, swipeRefreshLayout, manager)
        }
    }

    fun startRefreshing(key: String) {
        viewInstances[key]?.swipeRefreshLayout?.isRefreshing = true
    }

    fun stopRefreshing(key: String) {
        viewInstances[key]?.swipeRefreshLayout?.isRefreshing = false
    }

    fun setOnRefreshListener(key: String, onRefresh: () -> Unit = { }) {
        viewInstances[key]?.swipeRefreshLayout?.setOnRefreshListener { onRefresh() }
    }

    fun setNavigator(key: String, navigator: WebViewNavigator) {
        viewInstances[key]?.let { container ->
            navigator.apply {
                CoroutineScope(Dispatchers.Main).launch {
                    container.webView.handleNavigationEvents()
                }
            }
        }
    }

    fun setManager(key: String, block: ComposeWebViewManager.() -> Unit = {}) {
        viewInstances[key]?.let { container ->
            container.manager = ComposeWebViewManager(
                url = null,
                webView = container.webView,
                block = block
            )
        }
    }


}