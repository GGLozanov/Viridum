package com.lozanov.viridum.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lozanov.viridum.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

// no point after Android 12 SDK (SplashScreen API which can be called in MainActivity)
@Composable
fun Splash(onSplashFinish: () -> Unit) {
    val splashDuration = remember { 1500L }

    SplashContent()
    LaunchedEffect(keys = arrayOf(splashDuration)) {
        delay(splashDuration)
        onSplashFinish()
    }
}

@Composable
fun SplashContent() {
    Box(modifier = Modifier
            .fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.logo),
            modifier = Modifier
                .width(125.dp)
                .height(168.dp)
                .align(Alignment.Center)
        )
    }
}