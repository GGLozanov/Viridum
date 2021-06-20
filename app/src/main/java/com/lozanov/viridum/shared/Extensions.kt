package com.lozanov.viridum.shared

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.Fragment
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.KeyCharacterMap
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import com.google.ar.core.ArCoreApk
import com.lozanov.viridum.state.ARAvailabilityState
import kotlinx.coroutines.*
import pub.devrel.easypermissions.EasyPermissions

fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

fun ConnectivityManager.isConnected(): Boolean {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        with(
            getNetworkCapabilities(
                activeNetwork
            )
        ) {
            return this != null &&
                    hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    } else {
        return activeNetworkInfo?.isConnected == true // null safety requires explicit check
    }
}

fun Activity.requestPermissions(vararg perms: String, requestCode: Int, rationale: String,
                               onPermissionGranted: () -> Unit) {
    if(EasyPermissions.hasPermissions(
            this,
            *perms
        )) {
        onPermissionGranted()
    } else {
        showPermissionDialog(*perms, requestCode = requestCode, rationale = rationale)
    }
}

fun Fragment.requestPermissions(vararg perms: String, requestCode: Int, rationale: String,
                                onPermissionGranted: () -> Unit) {
    if(EasyPermissions.hasPermissions(
            requireContext(),
            *perms
        )) {
        onPermissionGranted()
    } else {
        showPermissionDialog(*perms, requestCode = requestCode, rationale = rationale)
    }
}

private fun Fragment.showPermissionDialog(vararg perms: String,
                                          requestCode: Int, rationale: String) {
    EasyPermissions.requestPermissions(
        this,
        rationale,
        requestCode,
        *perms
    )
}

private fun Activity.showPermissionDialog(vararg perms: String,
                                          requestCode: Int, rationale: String) {
    EasyPermissions.requestPermissions(
        this,
        rationale,
        requestCode,
        *perms
    )
}

// TODO: Use in entry of the app?
fun Context.isARCoreSupportedAndUpToDate(): ARAvailabilityState {
    return when (ArCoreApk.getInstance().checkAvailability(this)) {
        ArCoreApk.Availability.SUPPORTED_INSTALLED -> ARAvailabilityState.Installed
        ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD, ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED -> {
            try {
                // Request ARCore installation or update if needed.
                when (ArCoreApk.getInstance().requestInstall(getActivity(), true)) {
                    ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                        Log.i("isARCoreSupportedAndUpToDate", "ARCore installation requested.")
                        ARAvailabilityState.NotInstalledAndShouldTakeAction
                    }
                    ArCoreApk.InstallStatus.INSTALLED -> ARAvailabilityState.Installed
                }
            } catch (e: KeyCharacterMap.UnavailableException) {
                Log.e("isARCoreSupportedAndUpToDate", "ARCore not installed", e)
                ARAvailabilityState.NotInstalledAndShouldTakeAction
            }
        }

        ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE ->
            ARAvailabilityState.DeviceNotCapable

        ArCoreApk.Availability.UNKNOWN_CHECKING -> {
            ARAvailabilityState.Checking
        }
        ArCoreApk.Availability.UNKNOWN_ERROR, ArCoreApk.Availability.UNKNOWN_TIMED_OUT -> {
            ARAvailabilityState.ErrorOrTimedOut
        }
    }
}

fun Context.determineARAvailability(
    asked: State<Boolean>,
    coroutineScope: CoroutineScope,
): Boolean {
    if(!asked.value) {
        when(isARCoreSupportedAndUpToDate()) {
            is ARAvailabilityState.NotInstalledAndShouldTakeAction -> {
                // TODO: Prompt install (probably don't have to do anything?)
            }
            is ARAvailabilityState.DeviceNotCapable -> {
                // TODO: AlertDialog and suspend app
            }
            is ARAvailabilityState.ErrorOrTimedOut -> {
                // TODO: Snackbar
            }
            is ARAvailabilityState.Checking -> {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        delay(200)
                    }
                    determineARAvailability(asked,
                        coroutineScope)
                }
            }
            else -> return true
        }
        return false
    }
    return true
}

@Composable
fun ARCoreInstallationWrapper(
    askedForARCoreAvailability: State<Boolean>,
    errorSnackbarHostState: SnackbarHostState,
    onInstalled: @Composable () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // TODO: Pass snackbar state and whatever else is needed down
    if(!context.determineARAvailability(
            askedForARCoreAvailability, coroutineScope)) {
        return
    }

    onInstalled()
}

fun Context.getActivity(): AppCompatActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}