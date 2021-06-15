package com.lozanov.viridum

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lozanov.viridum.persistence.*
import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.Navigator
import com.lozanov.viridum.shared.isARCoreSupportedAndUpToDate
import com.lozanov.viridum.state.ARAvailabilityState
import com.lozanov.viridum.ui.auth.Login
import com.lozanov.viridum.ui.main.ARScreen
import com.lozanov.viridum.ui.main.ModelSelection
import com.lozanov.viridum.ui.main.main
import com.lozanov.viridum.ui.onboarding.Onboarding
import com.lozanov.viridum.ui.splash.Splash
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@ExperimentalPagerApi
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
    val isAuthenticated = context.readAuthToken().map { it == null }.collectAsState(initial = true)
    val askedForARAvailability = remember { mutableStateOf(false) }

    NavHost(navController = navController,
        startDestination = NavDestination.Splash.route) {
        composable(NavDestination.Splash.route) {
            BackHandler {
                exit()
            }

            Splash {
                // TODO: Add check for auth state in sharedprefs later on & immediately auth
                navigator.pop()
                navigator.navigate(if(shouldOnboard.value)
                    NavDestination.Onboarding else if(isAuthenticated.value) NavDestination.ModelSelection
                        else NavDestination.Login)
            }
        }
        composable(NavDestination.Onboarding.route) {
            Onboarding(
                onboardingComplete = {
                    coroutineScope.launch {
                        context.writeOnboardingValid(false)
                    }
                    navigator.pop()
                }
            )
        }
        // TODO: Encapsulate in separate nested graph
        composable(NavDestination.Login.route) {
            BackHandler {
                exit()
            }

            if (!shouldOnboard.value) {
                Login(onSuccessfulAuth = { token ->
                    coroutineScope.launch {
                        context.writeAuthToken(token)
                    }
                    navigator.pop()
                })
            }
        }
        // TODO: Encapsulate in separate nested graph
        main {

        }
    }
}