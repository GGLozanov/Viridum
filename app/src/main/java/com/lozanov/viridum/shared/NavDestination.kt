package com.lozanov.viridum.shared

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.navigation.compose.NamedNavArgument
import com.lozanov.viridum.R
import java.lang.IllegalArgumentException

sealed class NavDestination(val route: String,
                            val title: String? = null,
                            val arguments: List<NamedNavArgument> = emptyList()) {
    object Root : NavDestination("")

    object Splash : NavDestination("splash")

    object Onboarding : NavDestination("onboarding")

    // TODO: Context strings from values.xml for titles
    object Login : NavDestination("login", "Login")

    object ARScreen : NavDestination("ar_screen", "View")

    object ModelSelection : NavDestination("model_selection", "Select 3D Model")

    companion object {
        //  prolly kinda dumb but how else?
        fun findDestinationByRoute(route: String?): NavDestination {
            when(route) {
                Root.route -> Root
                Login.route -> Login
                Onboarding.route -> Onboarding
                ARScreen.route -> ARScreen
                ModelSelection.route -> ModelSelection
                else -> throw IllegalArgumentException("Invalid route for findDestinationByRoute call!")
            }
        }
    }
}
