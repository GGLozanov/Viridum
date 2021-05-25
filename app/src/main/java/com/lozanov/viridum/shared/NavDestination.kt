package com.lozanov.viridum.shared

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.navigation.compose.NamedNavArgument
import com.lozanov.viridum.R

sealed class NavDestination(val route: String,
                            val arguments: List<NamedNavArgument> = emptyList()) {
    object Root : NavDestination("")

    object Splash : NavDestination("splash")

    object Onboarding : NavDestination("onboarding")

    object Login : NavDestination("login")

    object ARScreen : NavDestination("ar_screen")

    object ModelSelection : NavDestination("model_selection")
}
