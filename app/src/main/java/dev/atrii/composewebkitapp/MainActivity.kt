package dev.atrii.composewebkitapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebSettings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.atrii.composewebkit.ComposeWebView
import dev.atrii.composewebkit.configureWebChromeClients
import dev.atrii.composewebkit.configureWebClients
import dev.atrii.composewebkit.configureWebSettings
import dev.atrii.composewebkit.rememberComposeWebViewState
import dev.atrii.composewebkit.rememberWebViewNavigator
import dev.atrii.composewebkitapp.ui.theme.ComposeWebKitTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeWebKitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                    AppWebView(
                        modifier = Modifier.padding(innerPadding),
                        url = "https://atrii.dev",
                        onFinishRequest = {}
                    )
                    return@Scaffold

                    var filePathsCallback by remember {
                        mutableStateOf<ValueCallback<Array<Uri>>?>(null)
                    }
                    var fileChooserCallbackFunction by remember {
                        mutableStateOf<((Array<Uri>?) -> Unit)?>(null)
                    }

                    val launcher =
                        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia()) { result ->
                            fileChooserCallbackFunction?.invoke(result.toTypedArray())
                            fileChooserCallbackFunction = null
                        }

                    var isRefreshing by rememberSaveable { mutableStateOf(false) }
                    val progress = remember { Animatable(0f) }
                    val scope = rememberCoroutineScope()
                    val navigator = rememberWebViewNavigator()
                    val state = rememberComposeWebViewState(
                        url = "https://poe.com/hanh_trinh_tin_mung",
                        onBackPress = {
                            if (navigator.canGoBack())
                                navigator.navigateBack()
                            else
                                finish()
                        }
                    ) {
                        configureWebSettings {
                            javaScriptEnabled = true
                            cacheMode = WebSettings.LOAD_NO_CACHE
                        }
                        configureWebClients {
                            onPageStarted { view, url, favicon ->
                                Log.d(TAG, "onCreate: $url")
                            }
                        }
                        configureWebChromeClients {
                            onShowFileChooser { _, webFilePathCallback, _ ->
                                filePathsCallback?.onReceiveValue(null)
                                filePathsCallback = webFilePathCallback

                                try {
                                    fileChooserCallbackFunction = { uris ->
                                        webFilePathCallback?.onReceiveValue(uris)
                                        filePathsCallback = null
                                    }

                                    launcher.launch(
                                        PickVisualMediaRequest(
                                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                    true
                                } catch (e: Exception) {
                                    webFilePathCallback?.onReceiveValue(null)
                                    filePathsCallback = null
                                    false
                                }
                            }
                            onProgressChanged { webView, newProgress ->
                                isRefreshing = newProgress in 1..99
                                scope.launch {
                                    progress.animateTo(newProgress.toFloat())
                                }
                            }

                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        ComposeWebView(
                            modifier = Modifier.weight(1f),
                            state = state,
                            navigator = navigator,
                            pull2Refresh = false,
                            key = "web1"
                        )

                    }

                }

            }

        }
    }


}