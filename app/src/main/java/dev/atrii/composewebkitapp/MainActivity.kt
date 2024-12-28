package dev.atrii.composewebkitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.atrii.composewebkit.ComposeWebView
import dev.atrii.composewebkit.configureWebSettings
import dev.atrii.composewebkit.rememberWebViewNavigator
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        var isRefreshing by rememberSaveable { mutableStateOf(false) }
                        val scope = rememberCoroutineScope()
                        val navigator = rememberWebViewNavigator()
                        val state = rememberComposeWebViewState(
                            url = "https://google.com",
                            onBackPress = {}
                        ) {
                            configureWebSettings {
                                javaScriptEnabled = true
                            }
                        }

                        ComposeWebView(
                            modifier = Modifier.fillMaxSize().weight(1f),
                            state = state,
                            pull2Refresh = true,
                            isRefreshing = isRefreshing,
                            navigator = navigator,
                            onRefresh = {
                                scope.launch {
                                    isRefreshing = true
                                    navigator.reload()
                                    delay(1000)
                                    isRefreshing = false
                                }
                            }
                        )

                        ComposeWebView(
                            modifier = Modifier.fillMaxSize().weight(1f),
                            state = state,
                            pull2Refresh = true,
                            isRefreshing = isRefreshing,
                            navigator = navigator,
                            onRefresh = {
                                scope.launch {
                                    isRefreshing = true
                                    navigator.reload()
                                    delay(1000)
                                    isRefreshing = false
                                }
                            }
                        )
                    }
                }

            }

        }
    }


}