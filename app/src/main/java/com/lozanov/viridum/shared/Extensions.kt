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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import com.google.ar.core.ArCoreApk
import com.lozanov.viridum.R
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
    errorSnackBarHostState: SnackbarHostState,
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
                coroutineScope.launch {
                    errorSnackBarHostState.showSnackbar(
                        message = resources.getString(R.string.error_ar)
                    )
                }
            }
            is ARAvailabilityState.Checking -> {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        delay(200)
                    }
                    determineARAvailability(asked, errorSnackBarHostState,
                        coroutineScope)
                }
            }
            else -> return true
        }
        return false
    }
    return true
}

interface ARCoreInstallationWrapperScope {
    val snackbarHostState: SnackbarHostState
}

@Composable
fun ARCoreInstallationWrapper(
    askedForARCoreAvailability: State<Boolean>,
    mainContentAlignment: Alignment = Alignment.Center,
    onInstalled: @Composable ARCoreInstallationWrapperScope.() -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val wrapperScope = remember { object : ARCoreInstallationWrapperScope {
        override val snackbarHostState: SnackbarHostState
            get() = snackbarHostState
    } }

    // TODO: Pass snackbar state and whatever else is needed down
    if(!context.determineARAvailability(
            askedForARCoreAvailability, snackbarHostState, coroutineScope)) {
        return
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = mainContentAlignment
    ) {
        onInstalled(p1 = wrapperScope)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
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

fun CoroutineScope.navPop(navigator: Navigator,
                          until: NavDestination? = null,
                          rawPop: Boolean = false,
                          onPop: (() -> Unit)? = null) {
    launch {
        navigator.pop(rawPop = rawPop, until = until)
        onPop?.invoke()
    }
}
