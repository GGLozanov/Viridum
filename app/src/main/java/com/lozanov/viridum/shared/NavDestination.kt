package com.lozanov.viridum.shared

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NamedNavArgument
import com.lozanov.viridum.R
import java.lang.IllegalArgumentException

sealed class NavDestination(val route: String = "",
                            @StringRes val titleId: Int? = null,
                            @StringRes val tabTitleId: Int? = null,
                            val arguments: List<NamedNavArgument> = emptyList(),
                            val hasBackArrow: Boolean = false,
                            val hasLogoutAction: Boolean = false) {
    object Root : NavDestination()

    object Splash : NavDestination("splash")

    object Onboarding : NavDestination("onboarding")

    object Login : NavDestination("login", R.string.login_title)

    object MainDestination : NavDestination("main") {
        object ARScreen : NavDestination("ar_screen", titleId = R.string.app_name,
            tabTitleId = R.string.view_model, hasLogoutAction = true)

        object ModelSelection : NavDestination("model_selection", R.string.app_name,
            tabTitleId = R.string.model_selection_title, hasLogoutAction = true)
    }

    companion object {
        const val TAG = "NavDestination"
        
        //  not actually a composable, just need the scoping provided
        @Composable
        fun findDestinationByRoute(route: String?): NavDestination {
            Log.i(TAG, "findDestinationByRoute -> route: ${route}")
            return when(route) {
                null, Root.route -> Root
                Login.route -> Login
                Onboarding.route -> Onboarding
                MainDestination.route -> MainDestination
                MainDestination.ARScreen.route -> MainDestination.ARScreen
                MainDestination.ModelSelection.route -> MainDestination.ModelSelection
                else -> throw IllegalArgumentException("Invalid route for findDestinationByRoute call!")
            }   
        }
    }
}
