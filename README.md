# ComposeWebKit (Dev Atrii)

https://github.com/user-attachments/assets/ab388668-7081-4ccf-80c3-0f9b1248237d

A modern WebView wrapper for Jetpack Compose that provides easy state management and configuration options.
[![YouTube Channel](https://img.shields.io/badge/YouTube-Subscribe-red?style=for-the-badge&logo=youtube)](https://www.youtube.com/@devatrii/videos)
[📺 Watch Tutorials & Development Content](https://www.youtube.com/@devatrii/videos)

[![](https://jitpack.io/v/DevAtrii/ComposeWebKit.svg)](https://jitpack.io/#DevAtrii/ComposeWebKit)
![badge-Android](https://img.shields.io/badge/Platform-Android-brightgreen)
![badge-Kotlin](https://img.shields.io/badge/Language-Kotlin-blue)
## Installation

### Kotlin DSL

1: Add jitpack repo in `settings.gradle.kts`
```kotlin
 maven (url = "https://jitpack.io")
```
2: Add dependency in `build.gradle.kts`
```kotlin
dependencies {
    implementation("com.github.DevAtrii:ComposeWebKit:2.0")
}
```

### Groovy

1: Add jitpack repo in `settings.gradle.kts`
```gropvy
 maven { url 'https://jitpack.io' }
```
2: Add dependency in `build.gradle.kts`
```gropvy
dependencies {
    implementation 'com.github.DevAtrii:ComposeWebKit:2.0'
}
```
⚠️ Don't forget to replace the version

### Permissions
Add Internet Permissions in `AndroidManifest.xml`
``` xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<application
    android:usesCleartextTraffic="true"
    ...
```

    
    
## Usage/Examples
Below is the simple example for `ComposeWebView`

```kotlin
val webState = rememberComposeWebViewState(
    url = "https://atrii.dev",
    handleBackPressEvents = true, // if true then call onBackPress else won't (true by default)
    onBackPress = { manager ->
        if (manager.webView.canGoBack())
            manager.webView.goBack()
        else
            finish()
    }
) {
    // Todo configure ComposeWebView Here
}

ComposeWebView(
    modifier = Modifier,
    webViewState = webState
)
```

### Configuring ComposeWebView
Just like `WebView` you can customise this. In order to customise we can use configure lambda in `webState`. In configure lambda we can use following functions:

| Function | Description | Common Use Cases |
|----------|-------------|-----------------|
| `configureWebClients { }` | Configures the WebViewClient which handles URL loading, errors, and page navigation events. | • Custom URL handling • Error handling • SSL certificate handling • Page start/finish events • Custom redirects |
| `configureWebChromeClients { }` | Configures the WebChromeClient which handles JavaScript dialogs, favicons, and progress events. | • Progress tracking • JavaScript alerts/confirms • Custom title handling • File upload handling • Fullscreen video support |
| `configureWebSettings { }` | Configures WebSettings which control the WebView's behavior and features. | • JavaScript enable/disable • Cache settings • Zoom controls • Text size • User-agent string • DOM storage |
| `configureWebView { }` | Direct access to configure the WebView instance itself. | • Layout parameters • Scroll settings • Hardware acceleration • Initial scale • Custom attributes |

Let's create ComposeWebView using above functions. We'll enable `cache`, `javascript` & implement `progress-indicator`.

``` kotlin
var progress by remember {
    mutableFloatStateOf(0f)
}
val scope = rememberCoroutineScope()


val webState = rememberComposeWebViewState(
    url = "https://atrii.dev",
    onBackPress = { manager ->
        if (manager.webView.canGoBack())
            manager.webView.goBack()
        else
            finish()
    },
){
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
    modifier = Modifier,
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


```

I've used `KOTLIN DSL` because it's syntax is easier to read & implement.



## License

MIT License

Copyright (c) 2024 Dev Atri

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

---

### 📱 Created & Maintained By

**Dev Atri**
* [YouTube Channel](https://www.youtube.com/@devatrii/videos) - Subscribe for Android & Kotlin development tutorials
* Follow for more Android development content and tutorials
