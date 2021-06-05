package com.lozanov.viridum

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.lozanov.viridum.persistence.dataStore
import com.lozanov.viridum.persistence.readOnboardingValid
import com.lozanov.viridum.persistence.writeOnboardingValid
import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.Navigator
import com.lozanov.viridum.ui.auth.Login
import com.lozanov.viridum.ui.main.ARScreen
import com.lozanov.viridum.ui.main.ModelSelectionScreen
import com.lozanov.viridum.ui.onboarding.Onboarding
import com.lozanov.viridum.ui.splash.Splash
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    navigator: Navigator,
    navController: NavHostController = rememberNavController(),
    exit: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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

    val shouldOnboard = context.readOnboardingValid().collectAsState(initial = true)

    NavHost(navController = navController,
        startDestination = NavDestination.Splash.route) {
        composable(NavDestination.Splash.route) {
            BackHandler {
                exit()
            }

            Splash {
                // TODO: Add check for auth state in sharedprefs later on & immediately auth
                navigator.navigate(if(shouldOnboard.value)
                    NavDestination.Onboarding else NavDestination.Login)
            }
        }
        composable(NavDestination.Onboarding.route) {
            Onboarding(
                onboardingComplete = {
                    coroutineScope.launch {
                        context.writeOnboardingValid(false)
                    }
                    // TODO: Same check for auth state
                    navigator.navigate(NavDestination.Login)
                }
            )
        }
        // TODO: Encapsulate in separate nested graph
        composable(NavDestination.Login.route) {
            BackHandler {
                exit()
            }

            Login()
        }
        // TODO: Encapsulate in separate nested graph
        composable(NavDestination.ModelSelection.route) {
            ModelSelectionScreen()
        }
        composable(NavDestination.ARScreen.route) {
            ARScreen()
        }
    }
}