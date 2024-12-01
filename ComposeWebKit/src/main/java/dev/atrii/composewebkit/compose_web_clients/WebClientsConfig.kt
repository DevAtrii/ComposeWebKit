package dev.atrii.composewebkit.compose_web_clients

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Message
import android.view.KeyEvent
import android.webkit.ClientCertRequest
import android.webkit.HttpAuthHandler
import android.webkit.RenderProcessGoneDetail
import android.webkit.SafeBrowsingResponse
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import dev.atrii.composewebkit.ComposeWebViewManager

/**
 * Configuration builder for WebViewClient with type-safe handlers for all WebViewClient callbacks.
 * Allows setting individual handlers for navigation, resource loading, errors, and other events.
 */
class WebClientsConfig(val manager: ComposeWebViewManager) {
    private var onPageStartedHandler: ((WebView?, String?, Bitmap?) -> Unit)? = null
    private var onPageFinishedHandler: ((WebView?, String?) -> Unit)? = null
    private var shouldOverrideUrlLoadingHandler: ((WebView?, WebResourceRequest?) -> Boolean)? =
        null
    private var onLoadResourceHandler: ((WebView?, String?) -> Unit)? = null
    private var onPageCommitVisibleHandler: ((WebView?, String?) -> Unit)? = null
    private var shouldInterceptRequestHandler: ((WebView?, WebResourceRequest?) -> WebResourceResponse?)? =
        null
    private var onReceivedErrorHandler: ((WebView?, WebResourceRequest?, WebResourceError?) -> Unit)? =
        null
    private var onReceivedHttpErrorHandler: ((WebView?, WebResourceRequest?, WebResourceResponse?) -> Unit)? =
        null
    private var onFormResubmissionHandler: ((WebView?, Message?, Message?) -> Unit)? = null
    private var doUpdateVisitedHistoryHandler: ((WebView?, String?, Boolean) -> Unit)? = null
    private var onReceivedSslErrorHandler: ((WebView?, SslErrorHandler?, SslError?) -> Unit)? = null
    private var onReceivedClientCertRequestHandler: ((WebView?, ClientCertRequest?) -> Unit)? = null
    private var onReceivedHttpAuthRequestHandler: ((WebView?, HttpAuthHandler?, String?, String?) -> Unit)? =
        null
    private var shouldOverrideKeyEventHandler: ((WebView?, KeyEvent?) -> Boolean)? = null
    private var onUnhandledKeyEventHandler: ((WebView?, KeyEvent?) -> Unit)? = null
    private var onScaleChangedHandler: ((WebView?, Float, Float) -> Unit)? = null
    private var onReceivedLoginRequestHandler: ((WebView?, String?, String?, String?) -> Unit)? =
        null
    private var onRenderProcessGoneHandler: ((WebView?, RenderProcessGoneDetail?) -> Boolean)? =
        null
    private var onSafeBrowsingHitHandler: ((WebView?, WebResourceRequest?, Int, SafeBrowsingResponse?) -> Unit)? =
        null

    fun onPageStarted(block: (view: WebView?, url: String?, favicon: Bitmap?) -> Unit) {
        onPageStartedHandler = block
    }

    fun onPageFinished(block: (view: WebView?, url: String?) -> Unit) {
        onPageFinishedHandler = block
    }

    fun shouldOverrideUrlLoading(block: (view: WebView?, request: WebResourceRequest?) -> Boolean) {
        shouldOverrideUrlLoadingHandler = block
    }

    fun onLoadResource(block: (view: WebView?, url: String?) -> Unit) {
        onLoadResourceHandler = block
    }

    fun onPageCommitVisible(block: (view: WebView?, url: String?) -> Unit) {
        onPageCommitVisibleHandler = block
    }

    fun shouldInterceptRequest(block: (view: WebView?, request: WebResourceRequest?) -> WebResourceResponse?) {
        shouldInterceptRequestHandler = block
    }

    fun onReceivedError(block: (view: WebView?, request: WebResourceRequest?, error: WebResourceError?) -> Unit) {
        onReceivedErrorHandler = block
    }

    fun onReceivedHttpError(block: (view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) -> Unit) {
        onReceivedHttpErrorHandler = block
    }

    fun onFormResubmission(block: (view: WebView?, dontResend: Message?, resend: Message?) -> Unit) {
        onFormResubmissionHandler = block
    }

    fun doUpdateVisitedHistory(block: (view: WebView?, url: String?, isReload: Boolean) -> Unit) {
        doUpdateVisitedHistoryHandler = block
    }

    fun onReceivedSslError(block: (view: WebView?, handler: SslErrorHandler?, error: SslError?) -> Unit) {
        onReceivedSslErrorHandler = block
    }

    fun onReceivedClientCertRequest(block: (view: WebView?, request: ClientCertRequest?) -> Unit) {
        onReceivedClientCertRequestHandler = block
    }

    fun onReceivedHttpAuthRequest(block: (view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) -> Unit) {
        onReceivedHttpAuthRequestHandler = block
    }

    fun shouldOverrideKeyEvent(block: (view: WebView?, event: KeyEvent?) -> Boolean) {
        shouldOverrideKeyEventHandler = block
    }

    fun onUnhandledKeyEvent(block: (view: WebView?, event: KeyEvent?) -> Unit) {
        onUnhandledKeyEventHandler = block
    }

    fun onScaleChanged(block: (view: WebView?, oldScale: Float, newScale: Float) -> Unit) {
        onScaleChangedHandler = block
    }

    fun onReceivedLoginRequest(block: (view: WebView?, realm: String?, account: String?, args: String?) -> Unit) {
        onReceivedLoginRequestHandler = block
    }

    fun onRenderProcessGone(block: (view: WebView?, detail: RenderProcessGoneDetail?) -> Boolean) {
        onRenderProcessGoneHandler = block
    }

    fun onSafeBrowsingHit(block: (view: WebView?, request: WebResourceRequest?, threatType: Int, callback: SafeBrowsingResponse?) -> Unit) {
        onSafeBrowsingHitHandler = block
    }

    internal fun build() = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            return shouldOverrideUrlLoadingHandler?.invoke(view, request)
                ?: super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            onPageFinishedHandler?.invoke(view, url)
                ?: super.onPageFinished(view, url)
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            onLoadResourceHandler?.invoke(view, url)
                ?: super.onLoadResource(view, url)
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            onPageCommitVisibleHandler?.invoke(view, url)
                ?: super.onPageCommitVisible(view, url)
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?,
        ): WebResourceResponse? {
            return shouldInterceptRequestHandler?.invoke(view, request)
                ?: super.shouldInterceptRequest(view, request)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?,
        ) {
            onReceivedErrorHandler?.invoke(view, request, error)
                ?: super.onReceivedError(view, request, error)
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?,
        ) {
            onReceivedHttpErrorHandler?.invoke(view, request, errorResponse)
                ?: super.onReceivedHttpError(view, request, errorResponse)
        }

        override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
            onFormResubmissionHandler?.invoke(view, dontResend, resend)
                ?: super.onFormResubmission(view, dontResend, resend)
        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            doUpdateVisitedHistoryHandler?.invoke(view, url, isReload)
                ?: super.doUpdateVisitedHistory(view, url, isReload)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?,
        ) {
            onReceivedSslErrorHandler?.invoke(view, handler, error)
                ?: super.onReceivedSslError(view, handler, error)
        }

        override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
            onReceivedClientCertRequestHandler?.invoke(view, request)
                ?: super.onReceivedClientCertRequest(view, request)
        }

        override fun onReceivedHttpAuthRequest(
            view: WebView?,
            handler: HttpAuthHandler?,
            host: String?,
            realm: String?,
        ) {
            onReceivedHttpAuthRequestHandler?.invoke(view, handler, host, realm)
                ?: super.onReceivedHttpAuthRequest(view, handler, host, realm)
        }

        override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
            return shouldOverrideKeyEventHandler?.invoke(view, event)
                ?: super.shouldOverrideKeyEvent(view, event)
        }

        override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
            onUnhandledKeyEventHandler?.invoke(view, event)
                ?: super.onUnhandledKeyEvent(view, event)
        }

        override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
            onScaleChangedHandler?.invoke(view, oldScale, newScale)
                ?: super.onScaleChanged(view, oldScale, newScale)
        }

        override fun onReceivedLoginRequest(
            view: WebView?,
            realm: String?,
            account: String?,
            args: String?,
        ) {
            onReceivedLoginRequestHandler?.invoke(view, realm, account, args)
                ?: super.onReceivedLoginRequest(view, realm, account, args)
        }

        override fun onRenderProcessGone(
            view: WebView?,
            detail: RenderProcessGoneDetail?,
        ): Boolean {
            return onRenderProcessGoneHandler?.invoke(view, detail)
                ?: super.onRenderProcessGone(view, detail)
        }

        override fun onSafeBrowsingHit(
            view: WebView?,
            request: WebResourceRequest?,
            threatType: Int,
            callback: SafeBrowsingResponse?,
        ) {
            onSafeBrowsingHitHandler?.invoke(view, request, threatType, callback)
                ?: super.onSafeBrowsingHit(view, request, threatType, callback)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            onPageStartedHandler?.invoke(view, url, favicon)
                ?: super.onPageStarted(view, url, favicon)
        }
    }
}