package com.lozanov.viridum

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lozanov.viridum.persistence.*
import com.lozanov.viridum.shared.NavAnimationFade
import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.Navigator
import com.lozanov.viridum.shared.navPop
import com.lozanov.viridum.ui.auth.Login
import com.lozanov.viridum.ui.main.main
import com.lozanov.viridum.ui.onboarding.Onboarding
import com.lozanov.viridum.ui.splash.Splash
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun NavGraph(
    navigator: Navigator,
    navController: NavHostController = rememberNavController(),
    exit: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = navigator) {
        navigator.latestNavigationState
            .distinctUntilChanged()
            .collectLatest { state ->
                Log.i("NavGraph", "navState: $state")
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
    }

    val hasOnboarded = context.readOnboardingValid().collectAsState(initial = false)
    val isAuthenticated = context.readAuthToken().map { it != null }.collectAsState(initial = false)
    val askedForARCoreAvailability = rememberSaveable { mutableStateOf(false) }

    if(!isAuthenticated.value && hasOnboarded.value
        && navController.currentDestination?.route != NavDestination.Splash.route) {
        coroutineScope.navPop(navigator, until = NavDestination.Login)
    }

    NavHost(navController = navController,
        startDestination = NavDestination.Splash.route
    ) {
        composable(NavDestination.Splash.route) {
            BackHandler {
                coroutineScope.navPop(navigator)
            }

            Splash {
                navController.popBackStack() // uhhhhhh, iffy but exception (hopefully)
                navigator.navigate(
                    when {
                        !hasOnboarded.value -> NavDestination.Onboarding
                        isAuthenticated.value -> NavDestination.MainDestination
                        else -> NavDestination.Login
                    }
                )
            }
        }
        composable(NavDestination.Onboarding.route) {
            Onboarding(
                back = {
                    coroutineScope.navPop(navigator)
                },
                onboardingComplete = {
                    coroutineScope.launch {
                        context.writeOnboardingValid(true)
                    }
                    coroutineScope.navPop(navigator)
                }
            )
        }
        composable(NavDestination.Login.route) {
            BackHandler {
                coroutineScope.navPop(navigator)
            }

            if (hasOnboarded.value) {
                NavAnimationFade(navigator = navigator) {
                    Login(askedForARCoreAvailability = askedForARCoreAvailability,
                        onSuccessfulAuth = { token ->
                            if(token != null) {
                                coroutineScope.launch {
                                    context.writeAuthToken(token)
                                }
                            }

                            coroutineScope.navPop(navigator)

                            navigator.navigate(NavDestination.MainDestination)
                        })
                }
            }
        }
        navigation(
            route = NavDestination.MainDestination.route,
            startDestination = NavDestination.MainDestination.ModelSelection.route
        ) {
            main(
                navigator = navigator,
                askedForARCoreAvailability = askedForARCoreAvailability,
            )
        }
    }
}