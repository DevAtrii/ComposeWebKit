package dev.atrii.composewebkit

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest

import android.webkit.WebView
import android.webkit.WebViewClient


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
class ComposeWebViewManager(
    url: String?,
    var webView: WebView,
    val block: ComposeWebViewManager.() -> Unit = {},
) {
    private var webClients: WebViewClient? = null
    private var onProgressChangedHandler: ((WebView?, Int) -> Unit)? = null

    private var webChromeClients: WebChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            onProgressChangedHandler?.invoke(view, newProgress)
        }
    }

    private var onErrorOccurs: (WebView?, WebResourceRequest?, WebResourceError?) -> Unit =
        { _, _, _ -> }
    private var onPageStarts: (WebView?, String?, Bitmap?) -> Unit = { _, _, _ -> }
    private var onPageFinishes: (WebView?, String?) -> Unit = { _, _ -> }


    init {
        if (url != null) {
            webView.loadUrl(url)
        }
        setupWebView()
    }


    private fun setupWebView() {
        if (webClients == null) {
            webClients = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    onPageStarts(
                        view,
                        url,
                        favicon
                    )
                }

                @SuppressLint("NewApi")
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?,
                ) {
                    super.onReceivedError(view, request, error)
                    onErrorOccurs(
                        view,
                        request,
                        error
                    )
                }


                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    onPageFinishes(
                        view,
                        url
                    )
                }

            }

        }
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

    internal fun updateOnErrorOccurs(handler: (WebView?, WebResourceRequest?, WebResourceError?) -> Unit) {
        onErrorOccurs = handler
    }

    internal fun updateOnPageStarts(handler: (WebView?, String?, Bitmap?) -> Unit) {
        onPageStarts = handler
    }

    internal fun updateOnPageFinishes(handler: (WebView?, String?) -> Unit) {
        onPageFinishes = handler
    }

    internal fun updateOnProgressChanged(handler: (WebView?, Int?) -> Unit) {
        onProgressChangedHandler = handler
    }

}
