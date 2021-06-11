package com.lozanov.viridum.shared

import android.app.Activity
import androidx.fragment.app.Fragment
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
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