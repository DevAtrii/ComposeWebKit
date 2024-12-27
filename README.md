
# ComposeWebKit (Dev Atrii)

https://github.com/user-attachments/assets/ab388668-7081-4ccf-80c3-0f9b1248237d

A modern WebView wrapper for Jetpack Compose that provides easy state management, multiple instance support, and rich configuration options.

[![](https://jitpack.io/v/DevAtrii/ComposeWebKit.svg)](https://jitpack.io/#DevAtrii/ComposeWebKit)
![badge-Android](https://img.shields.io/badge/Platform-Android-brightgreen)
![badge-Kotlin](https://img.shields.io/badge/Language-Kotlin-blue)

## Features

- 🌐 Multiple WebView instances support
- 🔄 Pull-to-refresh functionality
- 📱 Progress tracking
- ⚙️ Extensive configuration options
- 🎯 State preservation
- ↩️ Back navigation handling
- 🔒 SSL/Security handling
- 🎨 Custom client configurations

## Installation

### Kotlin DSL

1. Add JitPack repository in `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        // ...
        maven(url = "https://jitpack.io")
    }
}
```

2. Add dependency in `build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.DevAtrii:ComposeWebKit:latest-version")
}
```

### Required Permissions

Add these permissions to your `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<application
    android:usesCleartextTraffic="true"
    ...
>
```

## Basic Usage

```kotlin
val webState = rememberComposeWebViewState(
    url = "https://example.com",
    onBackPress = { manager ->
        if (manager.webView.canGoBack())
            manager.webView.goBack()
        else
            finish()
    }
) {
    // Configure WebView here
}

ComposeWebView(
    modifier = Modifier.fillMaxSize(),
    state = webState
)
```

## Advanced Usage

### Pull-to-Refresh

```kotlin
var isRefreshing by rememberSaveable { mutableStateOf(false) }
val scope = rememberCoroutineScope()
val navigator = rememberWebViewNavigator()

ComposeWebView(
    modifier = Modifier.fillMaxSize(),
    state = webState,
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
```

### Progress Tracking

```kotlin
var progress by remember { mutableFloatStateOf(0f) }

val webState = rememberComposeWebViewState(
    url = "https://example.com"
) {
    configureWebChromeClients {
        onProgressChanged { _, newProgress ->
            progress = newProgress.toFloat()
        }
    }
}

// Show progress indicator
AnimatedVisibility(
    visible = progress in 1f..99f
) {
    LinearProgressIndicator(
        progress = { progress / 100f }
    )
}
```

### WebView Configuration

```kotlin
val webState = rememberComposeWebViewState(
    url = "https://example.com"
) {
    configureWebSettings {
        javaScriptEnabled = true
        domStorageEnabled = true
        cacheMode = WebSettings.LOAD_DEFAULT
    }

    configureWebClients {
        onPageStarted { view, url, favicon ->
            // Handle page load start
        }
        onPageFinished { view, url ->
            // Handle page load completion
        }
    }

    configureWebChromeClients {
        onProgressChanged { _, progress ->
            // Handle progress updates
        }
        onReceivedTitle { _, title ->
            // Handle title updates
        }
    }
}
```

### Navigation

The `WebViewNavigator` provides methods to control WebView navigation programmatically:

```kotlin
val navigator = rememberWebViewNavigator()
val scope = rememberCoroutineScope()

// Navigation controls
Button(onClick = { 
    scope.launch {
        navigator.navigateTo("https://example.com")
    }
}) {
    Text("Navigate")
}

Row {
    IconButton(
        onClick = { 
            scope.launch {
                navigator.goBack()
            }
        },
        enabled = navigator.canGoBack
    ) {
        Icon(Icons.Default.ArrowBack, "Back")
    }
    
    IconButton(
        onClick = { 
            scope.launch {
                navigator.goForward()
            }
        },
        enabled = navigator.canGoForward
    ) {
        Icon(Icons.Default.ArrowForward, "Forward")
    }
    
    IconButton(onClick = { 
        scope.launch {
            navigator.reload()
        }
    }) {
        Icon(Icons.Default.Refresh, "Reload")
    }
    
    IconButton(onClick = { 
        scope.launch {
            navigator.stopLoading()
        }
    }) {
        Icon(Icons.Default.Close, "Stop")
    }
}

// Use with ComposeWebView
ComposeWebView(
    modifier = Modifier.fillMaxSize(),
    state = webState,
    navigator = navigator
)
```

Available navigation methods:
- `navigateTo(url: String)`: Navigate to a specific URL
- `goBack()`: Navigate back in history
- `goForward()`: Navigate forward in history
- `reload()`: Reload current page
- `stopLoading()`: Stop current loading
- `clearHistory()`: Clear navigation history
- `clearCache()`: Clear cache 

 


### Multiple WebView Instances

Each ComposeWebView instance can be uniquely identified using a key:

```kotlin
ComposeWebView(
    state = webState1,
    key = "webview1"
)

ComposeWebView(
    state = webState2,
    key = "webview2"
)
```

## License

```
MIT License

Copyright (c) 2024 Dev Atrii

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

 