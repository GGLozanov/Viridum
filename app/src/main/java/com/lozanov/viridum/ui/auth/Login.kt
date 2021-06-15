package com.lozanov.viridum.ui.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.lozanov.viridum.shared.ARCoreInstallationWrapper
import com.lozanov.viridum.shared.determineARAvailability


@Composable
fun Login(
    onSuccessfulAuth: (token: String) -> Unit,
    askedForARCoreAvailability: State<Boolean>
) {
    ARCoreInstallationWrapper(askedForARCoreAvailability) {
        val webViewVisible = remember { mutableStateOf(false) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            OutlinedButton(onClick = {
                webViewVisible.value = true
            }, border = BorderStroke(1.dp, Red),
                modifier = Modifier.padding(8.dp)) {
                Text(text = "Log in with SketchFab")
                // TODO: SketchFab Logo
                // Icon(painter = painterResource())
            }
        }

        if(webViewVisible.value) {
            SketchFabOAuthWebView(
                "https://sketchfab.com/oauth2/authorize/" +
                        "?state=123456789&response_type=token&client_id=" +
                        "${com.lozanov.viridum.BuildConfig.CLIENT_ID}",
                onSuccessfulTokenRetrieval = { url ->
                    // TODO: Process token
                    Log.i("Login", "${url}")

                    // onSuccessfulAuth(token)
                    webViewVisible.value = false
                }) {
                // TODO: Show error
                webViewVisible.value = false
            }
        }
    }
}

@Composable
fun SketchFabOAuthWebView(
    sketchFabUrl: String,
    onSuccessfulTokenRetrieval: (url: String) -> Unit,
    onFailedTokenRetrieval: () -> Unit
) {
    return AndroidView(factory = { context ->
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    // super.onPageFinished(view, url)
                    if(url != null && url.startsWith("http://localhost")) { // redirect URI
                        onSuccessfulTokenRetrieval(url)
                    } else {
                        onFailedTokenRetrieval()
                    }
                }
            }
            loadUrl(sketchFabUrl)
        }
    })
}