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
    private var shouldOverrideUrlLoadingStringHandler: ((WebView?, String?) -> Boolean)? = null
    private var shouldInterceptRequestStringHandler: ((WebView?, String?) -> WebResourceResponse?)? = null
    private var onReceivedErrorLegacyHandler: ((WebView?, Int, String?, String?) -> Unit)? = null
    private var onTooManyRedirectsHandler: ((WebView?, Message?, Message?) -> Unit)? = null

    /**
     * Sets a handler for when a page starts loading.
     *
     * @param block Handler that receives the WebView, URL, and favicon.
     * Called when the WebView begins to load a new page.
     */
    fun onPageStarted(block: (view: WebView?, url: String?, favicon: Bitmap?) -> Unit) {
        onPageStartedHandler = block
    }

    /**
     * Sets a handler for when a page finishes loading.
     *
     * @param block Handler that receives the WebView and the loaded URL.
     * Called when the WebView finishes loading a page.
     */
    fun onPageFinished(block: (view: WebView?, url: String?) -> Unit) {
        onPageFinishedHandler = block
    }

    /**
     * Sets a handler to determine if URL loading should be overridden.
     *
     * @param block Handler that receives the WebView and the resource request.
     * Should return true if the host application will handle the URL, false to let WebView handle it.
     * Called when the WebView is about to load a URL. Use this to intercept navigation.
     */
    fun shouldOverrideUrlLoading(block: (view: WebView?, request: WebResourceRequest?) -> Boolean) {
        shouldOverrideUrlLoadingHandler = block
    }

    /**
     * Sets a handler for when a resource is loaded.
     *
     * @param block Handler that receives the WebView and the resource URL.
     * Called when the WebView loads a resource (e.g., image, script, stylesheet).
     */
    fun onLoadResource(block: (view: WebView?, url: String?) -> Unit) {
        onLoadResourceHandler = block
    }

    /**
     * Sets a handler for when a page becomes visible to the user.
     *
     * @param block Handler that receives the WebView and the URL.
     * Called when the page's main frame has committed and is visible to the user.
     */
    fun onPageCommitVisible(block: (view: WebView?, url: String?) -> Unit) {
        onPageCommitVisibleHandler = block
    }

    /**
     * Sets a handler to intercept resource requests.
     *
     * @param block Handler that receives the WebView and the resource request.
     * Should return a WebResourceResponse to provide custom data, or null to use the default behavior.
     * Called when the WebView is about to load a resource. Use this to modify or replace resources.
     */
    fun shouldInterceptRequest(block: (view: WebView?, request: WebResourceRequest?) -> WebResourceResponse?) {
        shouldInterceptRequestHandler = block
    }

    /**
     * Sets a handler for when an error occurs while loading a resource.
     *
     * @param block Handler that receives the WebView, the resource request, and the error details.
     * Called when the WebView encounters an error while loading a resource.
     */
    fun onReceivedError(block: (view: WebView?, request: WebResourceRequest?, error: WebResourceError?) -> Unit) {
        onReceivedErrorHandler = block
    }

    /**
     * Sets a handler for when an HTTP error response is received.
     *
     * @param block Handler that receives the WebView, the resource request, and the error response.
     * Called when the WebView receives an HTTP error response (e.g., 404, 500).
     */
    fun onReceivedHttpError(block: (view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) -> Unit) {
        onReceivedHttpErrorHandler = block
    }

    /**
     * Sets a handler for when a form resubmission is requested.
     *
     * @param block Handler that receives the WebView and messages to cancel or resend the form.
     * Called when the user navigates back to a page that requires form resubmission.
     * Use the messages to cancel or resend the form data.
     */
    fun onFormResubmission(block: (view: WebView?, dontResend: Message?, resend: Message?) -> Unit) {
        onFormResubmissionHandler = block
    }

    /**
     * Sets a handler for updating the visited history.
     *
     * @param block Handler that receives the WebView, URL, and whether it's a reload.
     * Called when the WebView updates its visited history. Use this to track navigation history.
     */
    fun doUpdateVisitedHistory(block: (view: WebView?, url: String?, isReload: Boolean) -> Unit) {
        doUpdateVisitedHistoryHandler = block
    }

    /**
     * Sets a handler for SSL errors.
     *
     * @param block Handler that receives the WebView, SSL error handler, and the SSL error.
     * Called when the WebView encounters an SSL error. Use the handler to proceed or cancel.
     */
    fun onReceivedSslError(block: (view: WebView?, handler: SslErrorHandler?, error: SslError?) -> Unit) {
        onReceivedSslErrorHandler = block
    }

    /**
     * Sets a handler for client certificate requests.
     *
     * @param block Handler that receives the WebView and the client certificate request.
     * Called when the server requests a client certificate. Use the request to provide or cancel.
     */
    fun onReceivedClientCertRequest(block: (view: WebView?, request: ClientCertRequest?) -> Unit) {
        onReceivedClientCertRequestHandler = block
    }

    /**
     * Sets a handler for HTTP authentication requests.
     *
     * @param block Handler that receives the WebView, HTTP auth handler, host, and realm.
     * Called when the server requires HTTP authentication. Use the handler to provide credentials.
     */
    fun onReceivedHttpAuthRequest(block: (view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) -> Unit) {
        onReceivedHttpAuthRequestHandler = block
    }

    /**
     * Sets a handler to determine if a key event should be overridden.
     *
     * @param block Handler that receives the WebView and the key event.
     * Should return true if the host application will handle the key event, false otherwise.
     * Called when a key event is not handled by the WebView.
     */
    fun shouldOverrideKeyEvent(block: (view: WebView?, event: KeyEvent?) -> Boolean) {
        shouldOverrideKeyEventHandler = block
    }

    /**
     * Sets a handler for unhandled key events.
     *
     * @param block Handler that receives the WebView and the key event.
     * Called when a key event is not handled by the WebView and was not overridden.
     */
    fun onUnhandledKeyEvent(block: (view: WebView?, event: KeyEvent?) -> Unit) {
        onUnhandledKeyEventHandler = block
    }

    /**
     * Sets a handler for when the page scale changes.
     *
     * @param block Handler that receives the WebView, old scale, and new scale values.
     * Called when the zoom level of the page changes.
     */
    fun onScaleChanged(block: (view: WebView?, oldScale: Float, newScale: Float) -> Unit) {
        onScaleChangedHandler = block
    }

    /**
     * Sets a handler for login requests (HTTP authentication).
     *
     * @param block Handler that receives the WebView, realm, account, and arguments.
     * Called when the page requests HTTP authentication. This is a legacy method for login requests.
     */
    fun onReceivedLoginRequest(block: (view: WebView?, realm: String?, account: String?, args: String?) -> Unit) {
        onReceivedLoginRequestHandler = block
    }

    /**
     * Sets a handler for when the render process terminates unexpectedly.
     *
     * @param block Handler that receives the WebView and render process details.
     * Should return true if the host application will handle the termination, false otherwise.
     * Called when the WebView's render process crashes or is killed by the system.
     */
    fun onRenderProcessGone(block: (view: WebView?, detail: RenderProcessGoneDetail?) -> Boolean) {
        onRenderProcessGoneHandler = block
    }

    /**
     * Sets a handler for Safe Browsing threat detection.
     *
     * @param block Handler that receives the WebView, resource request, threat type, and response callback.
     * Called when Safe Browsing detects a potential threat. Use the callback to proceed or go back safely.
     */
    fun onSafeBrowsingHit(block: (view: WebView?, request: WebResourceRequest?, threatType: Int, callback: SafeBrowsingResponse?) -> Unit) {
        onSafeBrowsingHitHandler = block
    }

    /**
     * Sets a handler to determine if URL loading should be overridden (legacy string-based version).
     *
     * @param block Handler that receives the WebView and the URL string.
     * Should return true if the host application will handle the URL, false to let WebView handle it.
     * Called when the WebView is about to load a URL. This is a legacy overload of [shouldOverrideUrlLoading].
     */
    fun shouldOverrideUrlLoadingString(block: (view: WebView?, url: String?) -> Boolean) {
        shouldOverrideUrlLoadingStringHandler = block
    }

    /**
     * Sets a handler to intercept resource requests (legacy string-based version).
     *
     * @param block Handler that receives the WebView and the resource URL string.
     * Should return a WebResourceResponse to provide custom data, or null to use the default behavior.
     * Called when the WebView is about to load a resource. This is a legacy overload of [shouldInterceptRequest].
     */
    fun shouldInterceptRequestString(block: (view: WebView?, url: String?) -> WebResourceResponse?) {
        shouldInterceptRequestStringHandler = block
    }

    /**
     * Sets a handler for when an error occurs while loading a resource (legacy version).
     *
     * @param block Handler that receives the WebView, error code, description, and failing URL.
     * Called when the WebView encounters an error while loading a resource.
     * This is a legacy overload of [onReceivedError] that provides basic error information.
     */
    fun onReceivedErrorLegacy(block: (view: WebView?, errorCode: Int, description: String?, failingUrl: String?) -> Unit) {
        onReceivedErrorLegacyHandler = block
    }

    /**
     * Sets a handler for when too many redirects occur.
     *
     * @param block Handler that receives the WebView and messages to cancel or continue.
     * Called when the WebView encounters too many redirects while loading a page.
     * Use the messages to cancel the navigation or continue despite the redirects.
     */
    fun onTooManyRedirects(block: (view: WebView?, cancelMsg: Message?, continueMsg: Message?) -> Unit) {
        onTooManyRedirectsHandler = block
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

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            url: String?,
        ): Boolean {
            return shouldOverrideUrlLoadingStringHandler?.invoke(view, url)
                ?: super.shouldOverrideUrlLoading(view, url)
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            url: String?,
        ): WebResourceResponse? {
            return shouldInterceptRequestStringHandler?.invoke(view, url)
                ?: super.shouldInterceptRequest(view, url)
        }

        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?,
        ) {
            onReceivedErrorLegacyHandler?.invoke(view, errorCode, description, failingUrl)
                ?: super.onReceivedError(view, errorCode, description, failingUrl)
        }

        override fun onTooManyRedirects(
            view: WebView?,
            cancelMsg: Message?,
            continueMsg: Message?,
        ) {
            onTooManyRedirectsHandler?.invoke(view, cancelMsg, continueMsg)
                ?: super.onTooManyRedirects(view, cancelMsg, continueMsg)
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