package dev.atrii.composewebkit

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Stable


/**
 * Initializes a WebView with a ComposeWebViewManager.
 * @return Configured ComposeWebViewManager instance
 */
fun WebView.initWithManager(
    url: String? = null,
    block: ComposeWebViewManager.() -> Unit = {},
) =
    ComposeWebViewManager(
        webView = this,
        url = url,
        block = block
    )

/**
 * Manages WebView configuration and event handling.
 * Provides access to WebView clients and various callback handlers.
 */
@Stable
class ComposeWebViewManager(
    url: String?,
    var webView: WebView,
    val block: ComposeWebViewManager.() -> Unit = {},
) {
    private var webClients: WebViewClient? = object : WebViewClient(){}

    private var webChromeClients: WebChromeClient = object : WebChromeClient() {}

    init {
        if (url != null) {
            webView.loadUrl(url)
        }
        setupWebView()
    }


    private fun setupWebView() {
        webView.webViewClient = webClients!!
        webView.webChromeClient = webChromeClients
        this.block()
    }

    // internal functions for extensions
    internal fun updateWebClients(client: WebViewClient?) {
        webClients = client
        webView.webViewClient = webClients!!
    }

    internal fun updateWebChromeClients(client: WebChromeClient) {
        webChromeClients = client
        webView.webChromeClient = webChromeClients
    }


}
