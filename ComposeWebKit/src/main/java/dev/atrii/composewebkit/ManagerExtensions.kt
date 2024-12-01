package dev.atrii.composewebkit

import android.webkit.WebSettings
import android.webkit.WebView
import dev.atrii.composewebkit.compose_web_clients.WebChromeClientConfig
import dev.atrii.composewebkit.compose_web_clients.WebClientsConfig

/**
 * Extension functions for ComposeWebViewManager to configure different aspects of the WebView:
 */

/** Configure WebViewClient callbacks and behavior */
fun ComposeWebViewManager.configureWebClients(block: WebClientsConfig.() -> Unit) {
    val config = WebClientsConfig(this)
    config.block()
    updateWebClients(config.build())
}

/** Configure WebChromeClient callbacks and behavior */
fun ComposeWebViewManager.configureWebChromeClients(block: WebChromeClientConfig.() -> Unit) {
    val config = WebChromeClientConfig(this)
    config.block()
    updateWebChromeClients(config.build())
}

/** Configure WebView settings */
fun ComposeWebViewManager.configureWebSettings(block: WebSettings.() -> Unit) {
    webView.settings.apply(block)
}

/** Configure WebView instance directly */
fun ComposeWebViewManager.configureWebView(block: WebView.() -> Unit) {
    webView = webView.apply(block)
}
