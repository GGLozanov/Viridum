package com.lozanov.viridum.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lozanov.viridum.shared.NavDestination

fun NavGraphBuilder.main(
    onLogout: () -> Unit
) {
    // TODO: Use onLogout
    composable(NavDestination.ModelSelection.route) {
        ModelSelection()
    }
    composable(NavDestination.ARScreen.route) {
        ARScreen()
    }
}