package dev.atrii.composewebkit.compose_web_clients

import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.GeolocationPermissions
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import dev.atrii.composewebkit.ComposeWebViewManager

/**
 * Configuration builder for WebChromeClient with type-safe handlers for all WebChromeClient callbacks.
 * Handles progress updates, JavaScript dialogs, file choosing, permissions, and other UI-related events.
 */
class WebChromeClientConfig(private val manager: ComposeWebViewManager) {
    private var progressChangedHandler: ((WebView?, Int) -> Unit)? = null
    private var receivedTitleHandler: ((WebView?, String?) -> Unit)? = null
    private var receivedIconHandler: ((WebView?, Bitmap?) -> Unit)? = null
    private var receivedTouchIconUrlHandler: ((WebView?, String?, Boolean) -> Unit)? = null
    private var showCustomViewHandler: ((View?, WebChromeClient.CustomViewCallback?) -> Unit)? = null
    private var hideCustomViewHandler: (() -> Unit)? = null
    private var createWindowHandler: ((WebView?, Boolean, Boolean, Message?) -> Boolean)? = null
    private var jsAlertHandler: ((WebView?, String?, String?, JsResult?) -> Boolean)? = null
    private var jsConfirmHandler: ((WebView?, String?, String?, JsResult?) -> Boolean)? = null
    private var jsPromptHandler: ((WebView?, String?, String?, String?, JsPromptResult?) -> Boolean)? = null
    private var consoleMessageHandler: ((ConsoleMessage?) -> Boolean)? = null
    private var showFileChooserHandler: ((WebView?, ValueCallback<Array<Uri>>?, WebChromeClient.FileChooserParams?) -> Boolean)? = null
    private var permissionRequestHandler: ((PermissionRequest?) -> Unit)? = null
    private var geolocationPermissionsHandler: ((String?, GeolocationPermissions.Callback?) -> Unit)? = null

    fun onProgressChanged(block: (webView: WebView?, newProgress: Int) -> Unit) {
        progressChangedHandler = block
    }

    fun onReceivedTitle(block: (webView: WebView?, title: String?) -> Unit) {
        receivedTitleHandler = block
    }

    fun onReceivedIcon(block: (webView: WebView?, icon: Bitmap?) -> Unit) {
        receivedIconHandler = block
    }

    fun onReceivedTouchIconUrl(block: (webView: WebView?, url: String?, precomposed: Boolean) -> Unit) {
        receivedTouchIconUrlHandler = block
    }

    fun onShowCustomView(block: (view: View?, callback: WebChromeClient.CustomViewCallback?) -> Unit) {
        showCustomViewHandler = block
    }

    fun onHideCustomView(block: () -> Unit) {
        hideCustomViewHandler = block
    }

    fun onCreateWindow(block: (view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?) -> Boolean) {
        createWindowHandler = block
    }

    fun onJsAlert(block: (view: WebView?, url: String?, message: String?, result: JsResult?) -> Boolean) {
        jsAlertHandler = block
    }

    fun onJsConfirm(block: (view: WebView?, url: String?, message: String?, result: JsResult?) -> Boolean) {
        jsConfirmHandler = block
    }

    fun onJsPrompt(block: (view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?) -> Boolean) {
        jsPromptHandler = block
    }

    fun onConsoleMessage(block: (consoleMessage: ConsoleMessage?) -> Boolean) {
        consoleMessageHandler = block
    }

    fun onShowFileChooser(block: (webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: WebChromeClient.FileChooserParams?) -> Boolean) {
        showFileChooserHandler = block
    }

    fun onPermissionRequest(block: (request: PermissionRequest?) -> Unit) {
        permissionRequestHandler = block
    }

    fun onGeolocationPermissionsShowPrompt(block: (origin: String?, callback: GeolocationPermissions.Callback?) -> Unit) {
        geolocationPermissionsHandler = block
    }

    internal fun build(): WebChromeClient {
        return object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressChangedHandler?.invoke(view, newProgress) 
                    ?: super.onProgressChanged(view, newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                receivedTitleHandler?.invoke(view, title) 
                    ?: super.onReceivedTitle(view, title)
            }

            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                receivedIconHandler?.invoke(view, icon) 
                    ?: super.onReceivedIcon(view, icon)
            }

            override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
                receivedTouchIconUrlHandler?.invoke(view, url, precomposed) 
                    ?: super.onReceivedTouchIconUrl(view, url, precomposed)
            }

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                showCustomViewHandler?.invoke(view, callback) 
                    ?: super.onShowCustomView(view, callback)
            }

            override fun onHideCustomView() {
                hideCustomViewHandler?.invoke() 
                    ?: super.onHideCustomView()
            }

            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                return createWindowHandler?.invoke(view, isDialog, isUserGesture, resultMsg) 
                    ?: super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
            }

            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return jsAlertHandler?.invoke(view, url, message, result) 
                    ?: super.onJsAlert(view, url, message, result)
            }

            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return jsConfirmHandler?.invoke(view, url, message, result) 
                    ?: super.onJsConfirm(view, url, message, result)
            }

            override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
                return jsPromptHandler?.invoke(view, url, message, defaultValue, result) 
                    ?: super.onJsPrompt(view, url, message, defaultValue, result)
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                return consoleMessageHandler?.invoke(consoleMessage) 
                    ?: super.onConsoleMessage(consoleMessage)
            }

            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
                return showFileChooserHandler?.invoke(webView, filePathCallback, fileChooserParams) 
                    ?: super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
            }

            override fun onPermissionRequest(request: PermissionRequest?) {
                permissionRequestHandler?.invoke(request) 
                    ?: super.onPermissionRequest(request)
            }

            override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
                geolocationPermissionsHandler?.invoke(origin, callback) 
                    ?: super.onGeolocationPermissionsShowPrompt(origin, callback)
            }

        }
    }
}