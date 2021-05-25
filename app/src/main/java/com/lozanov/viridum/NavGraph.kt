package com.lozanov.viridum

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.Navigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun NavGraph(
    navigator: Navigator,
    navController: NavHostController = rememberNavController(),
    exit: () -> Unit
) {
    navigator.latestNavigationState
        .distinctUntilChanged()
        .collectAsState(initial = Navigator.LatestNavigationState.Idle).value.also { state ->
        when(state) {
            is Navigator.LatestNavigationState.Exit -> {
                exit()
            }
            is Navigator.LatestNavigationState.Navigate -> {
                navController.navigate(state.dest.route)
            }
            is Navigator.LatestNavigationState.Pop -> {
                if(state.until == null) {
                    navController.popBackStack()
                } else {
                    navController.popBackStack(state.until.route, false)
                }
            }
            is Navigator.LatestNavigationState.Idle -> {}
        }
    }

    // TODO: Read sharedprefs for login state & onboarding and change destination accordingly

    NavHost(navController = navController,
        startDestination = NavDestination.Splash.route) {
        // TODO: Add composables
    }
}

// NavComponent actions bundled
private class Actions {

}