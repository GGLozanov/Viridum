package com.lozanov.viridum.ui.main

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.ArCoreApk
import com.lozanov.viridum.viewmodel.ARViewModel
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.lozanov.viridum.R

@Composable
fun ARScreen(
    viewModel: ARViewModel = hiltViewModel()
) {
    // TODO: Camera request permissions as per https://stackoverflow.com/a/67120456/13417704
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = stringResource(R.string.ar_screen),
            modifier = Modifier.align(Alignment.Center))
    }
}

fun Context.createARSession() {
    // Create a new ARCore session.
    val session = Session(this)

    // Create a session config.
    val config = Config(session)

    // Do feature-specific operations here, such as enabling depth or turning on
    // support for Augmented Faces.

    // Configure the session.
    session.configure(config)

    // session.close()
}

//@Composable
//fun ARButton() {
//    val context = LocalContext.current
//    val availability = remember { mutableStateOf(ArCoreApk.getInstance()
//        .checkAvailability(context)) }
//
//    if (availability.value.isTransient) {
//        Handler(Looper.getMainLooper()).postDelayed({
//            // should recompose and re-run code?
//            availability.value = ArCoreApk.getInstance()
//                .checkAvailability(context)
//        }, 200)
//    }
//
//    if (availability.value.isSupported) {
//        IconButton(onClick = {
//            /*TODO*/
//        }) {
//            Icon(painter =
//                painterResource(id =
//                    com.lozanov.viridum.R.drawable.ic_baseline_3d_rotation_24),
//                contentDescription = stringResource(
//                    com.lozanov.viridum.R.string.three_dimensional)
//            )
//        }
//    }
//}