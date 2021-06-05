package com.lozanov.viridum.shared

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NamedNavArgument
import com.lozanov.viridum.R
import java.lang.IllegalArgumentException

sealed class NavDestination(val route: String = "",
                            @StringRes val titleId: Int? = null,
                            val arguments: List<NamedNavArgument> = emptyList(),
                            val tabDestination: Boolean = false) {
    object Root : NavDestination()

    object Splash : NavDestination("splash")

    object Onboarding : NavDestination("onboarding")

    object Login : NavDestination("login", R.string.login_title)

    object ARScreen : NavDestination("ar_screen", R.string.view_model, tabDestination = true)

    object ModelSelection : NavDestination("model_selection", R.string.model_selection_title, tabDestination = true)

    companion object {
        //  not actually a composable, just need the scoping provided
        @Composable
        fun findDestinationByRoute(route: String?): NavDestination =
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
