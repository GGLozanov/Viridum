package com.lozanov.viridum

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lozanov.viridum.ui.theme.ViridumTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.*
import com.lozanov.viridum.persistence.writeAuthToken
import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.NavDestination.Companion.findDestinationByRoute
import com.lozanov.viridum.shared.Navigator
import com.lozanov.viridum.shared.navPop
import com.lozanov.viridum.ui.main.ARScreen
import com.lozanov.viridum.ui.main.ModelSelection
import com.lozanov.viridum.ui.onboarding.OnboardingSegment
import com.lozanov.viridum.ui.onboarding.PageData
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun ViridumApplication(navigator: Navigator, exit: () -> Unit) {
    val navController = rememberNavController()

    ProvideWindowInsets {
        ViridumTheme {
            Scaffold(
                topBar = { AppBar(navigator, findDestinationByRoute(
                    navController.currentDestination?.route
                )) },
            ) {
                Column {
                    TabbedNav(navigator = navigator, navBackStackEntry =
                        navController.currentBackStackEntryAsState(),
                        routes = listOf(MainTabs.MODEL_SELECT, MainTabs.AR_SCREEN),
                    )
                    NavGraph(navigator,
                        navController, exit = exit)
                }
            }
        }
    }
}

@Composable
private fun AppBar(navigator: Navigator, currentDestination: NavDestination) {
    val title = currentDestination.titleId ?: return
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    TopAppBar(title = {
        Text(text = stringResource(title))
    },
        elevation = 0.dp,
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = {
            if(currentDestination.hasBackArrow) {
                IconButton(onClick = {
                    coroutineScope.launch {
                        navigator.pop()
                    }
                }) {
                    Icon(painter =
                        painterResource(R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = stringResource(R.string.back))
                }
            }
        },
        actions = {
            if(currentDestination.hasLogoutAction) {
                IconButton(onClick = {
                    coroutineScope.launch {
                        context.writeAuthToken(null)
                    }
                }) {
                    Icon(painter =
                        painterResource(id = R.drawable.ic_baseline_exit_to_app_24),
                    contentDescription = stringResource(R.string.logout))
                }
            }
        }
    )
}

@ExperimentalPagerApi
@Composable
private fun TabbedNav(navigator: Navigator,
                      navBackStackEntry: State<NavBackStackEntry?>,
                      routes: List<MainTabs>,
) {
    val currentRoute = navBackStackEntry.value?.destination?.route
    val currentDestination = routes.find { it.destination.route == currentRoute }
    val coroutineScope = rememberCoroutineScope()

    if (currentDestination != null) {
//        pagerState.value = rememberPagerState(pageCount = routes.size,
//            initialPage = currentDestination.ordinal)

        BackHandler {
            coroutineScope.navPop(navigator, rawPop = true)
        }
    
        TabRow(selectedTabIndex = currentDestination.ordinal, modifier =
                Modifier.navigationBarsHeight(additional = 56.dp)) {
            routes.forEach {
                val tabCurrentlySelected = it.ordinal == currentDestination.ordinal

                Tab(onClick = {
                    if(tabCurrentlySelected) {
                        return@Tab
                    }

                    navigator.navigate(it.destination,
                        navBackStackEntry.value)
                }, text = { val titleRes = it.destination.tabTitleId
                    if(titleRes != null) {
                        Text(text = stringResource(titleRes)) } }, icon = { Icon(
                    painter = painterResource(it.icon), contentDescription = null) },
                        selected = tabCurrentlySelected)
            }
        }

        // TODO: Horizontal pager tabs when generating Composables in nav graph and Pager I figure out
    }
}

private enum class MainTabs(@DrawableRes val icon: Int,
                            val destination: NavDestination) {
    MODEL_SELECT(R.drawable.ic_baseline_file_copy_24, NavDestination.MainDestination.ModelSelection),
    AR_SCREEN(R.drawable.ic_baseline_3d_rotation_24, NavDestination.MainDestination.ARScreen)
}