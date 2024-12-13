package dev.atrii.composewebkitapp

import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.atrii.composewebkit.ComposeWebView
import dev.atrii.composewebkit.configureWebChromeClients
import dev.atrii.composewebkit.configureWebSettings
import dev.atrii.composewebkit.rememberComposeWebViewState
import dev.atrii.composewebkitapp.ui.theme.ComposeWebKitTheme
import kotlinx.coroutines.delay
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
                    var progress by remember {
                        mutableFloatStateOf(0f)
                    }
                    val scope = rememberCoroutineScope()
                    val webState = rememberComposeWebViewState(
                        url = "https://www.youtube.com/@devatrii/videos",
                        onBackPress = { manager ->
                            if (manager.webView.canGoBack())
                                manager.webView.goBack()
                            else
                                finish()
                        },
                    ) {
                        configureWebSettings {
                            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                            javaScriptEnabled = true
                        }
                        configureWebChromeClients {
                            onProgressChanged { webView, newProgress ->
                                scope.launch {
                                    progress = newProgress.toFloat()
                                }
                            }
                        }
                    }
                    ComposeWebView(
                        modifier = Modifier.padding(innerPadding),
                        webViewState = webState
                    )

                    AnimatedVisibility(
                        visible = progress in 1f..99f,
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LinearProgressIndicator(
                                progress = { progress / 100f },
                            )
                        }
                    }
                }
            }
        }
    }
}