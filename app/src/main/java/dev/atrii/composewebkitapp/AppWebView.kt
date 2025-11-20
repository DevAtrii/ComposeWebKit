package dev.atrii.composewebkitapp

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.HttpAuthHandler
import android.webkit.ValueCallback
import android.webkit.WebSettings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import dev.atrii.composewebkit.ComposeWebView
import dev.atrii.composewebkit.configureWebChromeClients
import dev.atrii.composewebkit.configureWebClients
import dev.atrii.composewebkit.configureWebSettings
import dev.atrii.composewebkit.configureWebView
import dev.atrii.composewebkit.rememberComposeWebViewState
import dev.atrii.composewebkit.rememberWebViewNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.webkit.PermissionRequest
import androidx.compose.foundation.layout.imePadding

private const val TAG = "AppWebView"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AppWebView(
    url: String,
    modifier: Modifier = Modifier,
    allowDownloads: Boolean = false,
    cache: Boolean = true,
    enableProgressBar: Boolean = true,
    showP2RWithoutRefreshing: Boolean = false,
    pull2Refresh: Boolean = false,
    onFinishRequest: () -> Unit,
) {
    val backPressTimer = 2000L
    var backPressCount by remember {
        mutableIntStateOf(0)
    }
    val scope = rememberCoroutineScope()


    // webview
    val navigator = rememberWebViewNavigator()
    val progress = remember { Animatable(0f) }
    var isRefreshing by rememberSaveable {
        mutableStateOf(false)
    }
    var isRefreshed by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(isRefreshed) {
        delay(1000)
        isRefreshed = false
    }
    // file picker
    var filePathsCallback by remember {
        mutableStateOf<ValueCallback<Array<Uri>>?>(null)
    }
    var fileChooserCallbackFunction by remember {
        mutableStateOf<((Array<Uri>?) -> Unit)?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { result ->
            fileChooserCallbackFunction?.invoke(result.toTypedArray())
            fileChooserCallbackFunction = null
        }

    // http auth
    var authHost by rememberSaveable {
        mutableStateOf(url)
    }
    var showAuthDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var httpAuthHandler: HttpAuthHandler? by remember {
        mutableStateOf(null)
    }

    val context = LocalContext.current
    var cameraPermissionRequest: PermissionRequest? = null
    var showReloadDialog by remember { mutableStateOf(false) }

    // Permission request launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d(TAG, "AppWebView: cameraPermissionResult: $isGranted")
        if (isGranted) {
            if (cameraPermissionRequest == null) {
                Log.d(TAG, "AppWebView: cameraPermissionRequest is null")
                // Show dialog to reload the page since we need to refresh after first permission grant
                showReloadDialog = true
                return@rememberLauncherForActivityResult
            }
            val permissions = cameraPermissionRequest?.resources
            if (permissions == null) {
                Log.d(TAG, "AppWebView: permissions are null")
                return@rememberLauncherForActivityResult
            }
            Log.d(TAG, "AppWebView: granting permissions")
            cameraPermissionRequest!!.grant(permissions)
        } else {
            cameraPermissionRequest?.deny()
        }
    }

    val state = rememberComposeWebViewState(
        url = url,
        onBackPress = {
            scope.launch {
                if (navigator.canGoBack()) {
                    navigator.navigateBack()
                } else {
                    backPressCount++
                    if (backPressCount == 2) {
                        onFinishRequest()
                        return@launch
                    }
                    delay(backPressTimer)
                    backPressCount = 0
                }
            }
        }
    ) {
        configureWebView {
            webView = this
        }
        configureWebSettings {
            javaScriptEnabled = true
            domStorageEnabled = true
            if (cache)
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            // Enable media support
            mediaPlaybackRequiresUserGesture = false
        }

        configureWebClients {
            onReceivedHttpAuthRequest { _, handler, host, _ ->
                handler ?: return@onReceivedHttpAuthRequest
                httpAuthHandler = handler
                showAuthDialog = true
                authHost = host ?: authHost
            }

        }

        configureWebChromeClients {
            onPermissionRequest { request ->
                val permissions = request?.resources ?: return@onPermissionRequest
                if (permissions.contains("android.webkit.resource.VIDEO_CAPTURE")) {
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            Log.d(
                                TAG,
                                "AppWebView: Permission already granted, granting to webview"
                            )
                            request.grant(permissions)
                        }

                        else -> {
                            Log.d(TAG, "AppWebView: asking for camera permission")
                            cameraPermissionRequest = request
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                } else {
                    // If it's not camera permission, deny the request
                    request.deny()
                }
            }
            onProgressChanged { _, newProgress ->
                scope.launch {
                    isRefreshing = when {
                        isRefreshed -> newProgress in 1..99
                        showP2RWithoutRefreshing -> newProgress in 1..99
                        else -> false
                    }
                    progress.animateTo(newProgress.toFloat())
                }
            }
            onShowFileChooser { _, webFilePathCallback, _ ->
                filePathsCallback?.onReceiveValue(null)
                filePathsCallback = webFilePathCallback
                try {
                    fileChooserCallbackFunction = { uris ->
                        webFilePathCallback?.onReceiveValue(uris)
                        filePathsCallback = null
                    }
                    launcher.launch(
                        input = "*/*",
                    )
                    true
                } catch (e: Exception) {
                    webFilePathCallback?.onReceiveValue(null)
                    filePathsCallback = null
                    false
                }
            }
        }
    }



    ComposeWebView(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        state = state,
        navigator = navigator,
        pull2Refresh = pull2Refresh,
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshed = true
            navigator.reload()
        }
    )


    if (!enableProgressBar)
        return


}


