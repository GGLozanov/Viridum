package com.lozanov.viridum.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.ArCoreApk
import com.lozanov.viridum.viewmodel.ARViewModel
import android.os.Handler
import android.os.Looper
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun ARScreen(
    viewModel: ARViewModel = hiltViewModel()
) {
    // TODO: Camera request permissions as per https://stackoverflow.com/a/67120456/13417704
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