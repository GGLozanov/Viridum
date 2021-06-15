package com.lozanov.viridum.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lozanov.viridum.shared.NavDestination

fun NavGraphBuilder.main(
    onLogout: () -> Unit,
    askedForARCoreAvailability: State<Boolean>
) {
    // TODO: Use onLogout
    composable(NavDestination.ModelSelection.route) {
        ModelSelection(
            askedForARCoreAvailability = askedForARCoreAvailability)
    }
    composable(NavDestination.ARScreen.route) {
        ARScreen()
    }
}