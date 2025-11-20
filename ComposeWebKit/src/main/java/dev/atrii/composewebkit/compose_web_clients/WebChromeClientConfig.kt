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
import android.webkit.WebStorage
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
    private var showCustomViewWithOrientationHandler: ((View?, Int, WebChromeClient.CustomViewCallback?) -> Unit)? = null
    private var consoleMessageStringHandler: ((String?, Int, String?) -> Unit)? = null
    private var requestFocusHandler: ((WebView?) -> Unit)? = null
    private var closeWindowHandler: ((WebView?) -> Unit)? = null
    private var jsBeforeUnloadHandler: ((WebView?, String?, String?, JsResult?) -> Boolean)? = null
    private var exceededDatabaseQuotaHandler: ((String?, String?, Long, Long, Long, WebStorage.QuotaUpdater?) -> Unit)? = null
    private var geolocationPermissionsHidePromptHandler: (() -> Unit)? = null
    private var permissionRequestCanceledHandler: ((PermissionRequest?) -> Unit)? = null
    private var jsTimeoutHandler: (() -> Boolean)? = null
    private var defaultVideoPosterHandler: (() -> Bitmap?)? = null
    private var videoLoadingProgressViewHandler: (() -> View?)? = null
    private var visitedHistoryHandler: ((ValueCallback<Array<out String?>?>?) -> Unit)? = null


    /**
     * Sets a handler for when the loading progress of the WebView changes.
     *
     * @param block Handler that receives the WebView and the new progress value (0-100).
     * Called whenever the loading progress of the page changes.
     */
    fun onProgressChanged(block: (webView: WebView?, newProgress: Int) -> Unit) {
        progressChangedHandler = block
    }

    /**
     * Sets a handler for when the page title is received.
     *
     * @param block Handler that receives the WebView and the page title.
     * Called when the WebView receives a new page title.
     */
    fun onReceivedTitle(block: (webView: WebView?, title: String?) -> Unit) {
        receivedTitleHandler = block
    }

    /**
     * Sets a handler for when the page icon (favicon) is received.
     *
     * @param block Handler that receives the WebView and the icon Bitmap.
     * Called when the WebView receives a new page icon.
     */
    fun onReceivedIcon(block: (webView: WebView?, icon: Bitmap?) -> Unit) {
        receivedIconHandler = block
    }

    /**
     * Sets a handler for when a touch icon URL is received.
     *
     * @param block Handler that receives the WebView, the icon URL, and whether it's precomposed.
     * Called when the WebView receives a touch icon URL for the page.
     */
    fun onReceivedTouchIconUrl(block: (webView: WebView?, url: String?, precomposed: Boolean) -> Unit) {
        receivedTouchIconUrlHandler = block
    }

    /**
     * Sets a handler for when a custom view should be shown (e.g., for fullscreen video).
     *
     * @param block Handler that receives the custom View and a callback to exit the custom view.
     * Called when the page wants to show a custom view, typically for fullscreen video playback.
     * Use the callback to exit the custom view when done.
     */
    fun onShowCustomView(block: (view: View?, callback: WebChromeClient.CustomViewCallback?) -> Unit) {
        showCustomViewHandler = block
    }

    /**
     * Sets a handler for when the custom view should be hidden.
     *
     * @param block Handler called when the custom view should be hidden.
     * Called when the page wants to hide the custom view that was previously shown.
     */
    fun onHideCustomView(block: () -> Unit) {
        hideCustomViewHandler = block
    }

    /**
     * Sets a handler for when a new window is requested by the page.
     *
     * @param block Handler that receives the WebView, whether it's a dialog, whether it's a user gesture,
     * and a result message. Should return true if the host application will create the window.
     * Called when the page requests a new window to be created.
     */
    fun onCreateWindow(block: (view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?) -> Boolean) {
        createWindowHandler = block
    }

    /**
     * Sets a handler for JavaScript alert dialogs.
     *
     * @param block Handler that receives the WebView, URL, message, and JsResult.
     * Should return true if the host application will handle the alert, false otherwise.
     * Called when a JavaScript alert() is executed.
     */
    fun onJsAlert(block: (view: WebView?, url: String?, message: String?, result: JsResult?) -> Boolean) {
        jsAlertHandler = block
    }

    /**
     * Sets a handler for JavaScript confirm dialogs.
     *
     * @param block Handler that receives the WebView, URL, message, and JsResult.
     * Should return true if the host application will handle the confirm, false otherwise.
     * Called when a JavaScript confirm() is executed.
     */
    fun onJsConfirm(block: (view: WebView?, url: String?, message: String?, result: JsResult?) -> Boolean) {
        jsConfirmHandler = block
    }

    /**
     * Sets a handler for JavaScript prompt dialogs.
     *
     * @param block Handler that receives the WebView, URL, message, default value, and JsPromptResult.
     * Should return true if the host application will handle the prompt, false otherwise.
     * Called when a JavaScript prompt() is executed.
     */
    fun onJsPrompt(block: (view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?) -> Boolean) {
        jsPromptHandler = block
    }

    /**
     * Sets a handler for console messages from JavaScript.
     *
     * @param block Handler that receives the ConsoleMessage and should return true
     * if the message was handled, false otherwise.
     * Called when a console message is generated by JavaScript code.
     */
    fun onConsoleMessage(block: (consoleMessage: ConsoleMessage?) -> Boolean) {
        consoleMessageHandler = block
    }

    /**
     * Sets a handler for file chooser requests.
     *
     * @param block Handler that receives the WebView, file path callback, and file chooser parameters.
     * Should return true if the host application will handle the file chooser, false otherwise.
     * Called when the page requests a file chooser (e.g., for file uploads).
     * Use the callback to return the selected file URIs.
     */
    fun onShowFileChooser(block: (webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: WebChromeClient.FileChooserParams?) -> Boolean) {
        showFileChooserHandler = block
    }

    /**
     * Sets a handler for permission requests (e.g., camera, microphone).
     *
     * @param block Handler that receives the PermissionRequest.
     * Called when the page requests permissions for resources like camera or microphone.
     * Use [PermissionRequest.grant] or [PermissionRequest.deny] to respond.
     */
    fun onPermissionRequest(block: (request: PermissionRequest?) -> Unit) {
        permissionRequestHandler = block
    }

    /**
     * Sets a handler for geolocation permission prompts.
     *
     * @param block Handler that receives the origin and a callback to grant or deny permission.
     * Called when the page requests geolocation permission.
     * Use the callback to grant or deny the permission.
     */
    fun onGeolocationPermissionsShowPrompt(block: (origin: String?, callback: GeolocationPermissions.Callback?) -> Unit) {
        geolocationPermissionsHandler = block
    }

    /**
     * Sets a handler for when a custom view should be shown with a specific orientation.
     *
     * @param block Handler that receives the custom View, requested orientation, and a callback.
     * Called when the page wants to show a custom view with a specific screen orientation.
     * This is an overload of [onShowCustomView] that includes orientation information.
     */
    fun onShowCustomViewWithOrientation(block: (view: View?, requestedOrientation: Int, callback: WebChromeClient.CustomViewCallback?) -> Unit) {
        showCustomViewWithOrientationHandler = block
    }

    /**
     * Sets a handler for console messages from JavaScript (legacy string-based version).
     *
     * @param block Handler that receives the message, line number, and source ID.
     * Called when a console message is generated by JavaScript code.
     * This is a legacy overload of [onConsoleMessage] that provides string-based information.
     */
    fun onConsoleMessageString(block: (message: String?, lineNumber: Int, sourceID: String?) -> Unit) {
        consoleMessageStringHandler = block
    }

    /**
     * Sets a handler for when the WebView requests focus.
     *
     * @param block Handler that receives the WebView requesting focus.
     * Called when the WebView needs to request focus from the system.
     */
    fun onRequestFocus(block: (view: WebView?) -> Unit) {
        requestFocusHandler = block
    }

    /**
     * Sets a handler for when a window should be closed.
     *
     * @param block Handler that receives the WebView window to be closed.
     * Called when the page requests that a window be closed.
     */
    fun onCloseWindow(block: (window: WebView?) -> Unit) {
        closeWindowHandler = block
    }

    /**
     * Sets a handler for the JavaScript beforeunload event.
     *
     * @param block Handler that receives the WebView, URL, message, and JsResult.
     * Should return true if the host application will handle the beforeunload, false otherwise.
     * Called when the page's beforeunload event is triggered, typically when navigating away.
     */
    fun onJsBeforeUnload(block: (view: WebView?, url: String?, message: String?, result: JsResult?) -> Boolean) {
        jsBeforeUnloadHandler = block
    }

    /**
     * Sets a handler for when the database quota is exceeded.
     *
     * @param block Handler that receives the URL, database identifier, quota information,
     * and a quota updater to adjust the quota if needed.
     * Called when a web database exceeds its quota. Use the quotaUpdater to grant more space.
     */
    fun onExceededDatabaseQuota(block: (url: String?, databaseIdentifier: String?, quota: Long, estimatedDatabaseSize: Long, totalQuota: Long, quotaUpdater: WebStorage.QuotaUpdater?) -> Unit) {
        exceededDatabaseQuotaHandler = block
    }

    /**
     * Sets a handler for when the geolocation permissions prompt should be hidden.
     *
     * @param block Handler called when the geolocation permissions prompt should be dismissed.
     * Called when the geolocation permissions prompt should be hidden.
     */
    fun onGeolocationPermissionsHidePrompt(block: () -> Unit) {
        geolocationPermissionsHidePromptHandler = block
    }

    /**
     * Sets a handler for when a permission request is canceled.
     *
     * @param block Handler that receives the canceled PermissionRequest.
     * Called when a permission request is canceled (e.g., user dismissed the permission dialog).
     */
    fun onPermissionRequestCanceled(block: (request: PermissionRequest?) -> Unit) {
        permissionRequestCanceledHandler = block
    }

    /**
     * Sets a handler for JavaScript timeout events.
     *
     * @param block Handler that should return true if the timeout was handled, false otherwise.
     * Called when a JavaScript timeout occurs. Return true to prevent the default timeout behavior.
     */
    fun onJsTimeout(block: () -> Boolean) {
        jsTimeoutHandler = block
    }

    /**
     * Sets a handler to provide a default poster image for video elements.
     *
     * @param block Handler that should return a Bitmap to use as the default video poster,
     * or null to use the default behavior.
     * Called when the WebView needs a default poster image for a video element.
     */
    fun getDefaultVideoPoster(block: () -> Bitmap?) {
        defaultVideoPosterHandler = block
    }

    /**
     * Sets a handler to provide a custom view shown while a video is loading.
     *
     * @param block Handler that should return a View to display while video is loading,
     * or null to use the default behavior.
     * Called when the WebView needs a view to show while a video element is loading.
     */
    fun getVideoLoadingProgressView(block: () -> View?) {
        videoLoadingProgressViewHandler = block
    }

    /**
     * Sets a handler to provide the visited history for the WebView.
     *
     * @param block Handler that receives a callback to provide the visited URLs.
     * Called when the WebView needs to know which URLs have been visited.
     * Use the callback to provide an array of visited URL strings.
     */
    fun getVisitedHistory(block: (callback: ValueCallback<Array<out String?>?>?) -> Unit) {
        visitedHistoryHandler = block
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

            override fun onShowCustomView(
                view: View?,
                requestedOrientation: Int,
                callback: CustomViewCallback?,
            ) {
                showCustomViewWithOrientationHandler?.invoke(view, requestedOrientation, callback)
                    ?: super.onShowCustomView(view, requestedOrientation, callback)
            }

            override fun onConsoleMessage(
                message: String?,
                lineNumber: Int,
                sourceID: String?,
            ) {
                consoleMessageStringHandler?.invoke(message, lineNumber, sourceID)
                    ?: super.onConsoleMessage(message, lineNumber, sourceID)
            }

            override fun onRequestFocus(view: WebView?) {
                requestFocusHandler?.invoke(view)
                    ?: super.onRequestFocus(view)
            }

            override fun onCloseWindow(window: WebView?) {
                closeWindowHandler?.invoke(window)
                    ?: super.onCloseWindow(window)
            }

            override fun onJsBeforeUnload(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?,
            ): Boolean {
                return jsBeforeUnloadHandler?.invoke(view, url, message, result)
                    ?: super.onJsBeforeUnload(view, url, message, result)
            }

            override fun onExceededDatabaseQuota(
                url: String?,
                databaseIdentifier: String?,
                quota: Long,
                estimatedDatabaseSize: Long,
                totalQuota: Long,
                quotaUpdater: WebStorage.QuotaUpdater?,
            ) {
                exceededDatabaseQuotaHandler?.invoke(
                    url,
                    databaseIdentifier,
                    quota,
                    estimatedDatabaseSize,
                    totalQuota,
                    quotaUpdater
                ) ?: super.onExceededDatabaseQuota(
                    url,
                    databaseIdentifier,
                    quota,
                    estimatedDatabaseSize,
                    totalQuota,
                    quotaUpdater
                )
            }

            override fun onGeolocationPermissionsHidePrompt() {
                geolocationPermissionsHidePromptHandler?.invoke()
                    ?: super.onGeolocationPermissionsHidePrompt()
            }

            override fun onPermissionRequestCanceled(request: PermissionRequest?) {
                permissionRequestCanceledHandler?.invoke(request)
                    ?: super.onPermissionRequestCanceled(request)
            }

            override fun onJsTimeout(): Boolean {
                return jsTimeoutHandler?.invoke()
                    ?: super.onJsTimeout()
            }

            override fun getDefaultVideoPoster(): Bitmap? {
                return defaultVideoPosterHandler?.invoke()
                    ?: super.getDefaultVideoPoster()
            }

            override fun getVideoLoadingProgressView(): View? {
                return videoLoadingProgressViewHandler?.invoke()
                    ?: super.getVideoLoadingProgressView()
            }

            override fun getVisitedHistory(callback: ValueCallback<Array<out String?>?>?) {
                visitedHistoryHandler?.invoke(callback)
                    ?: super.getVisitedHistory(callback)
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