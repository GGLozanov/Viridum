package com.lozanov.viridum

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lozanov.viridum.ui.theme.ViridumTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.NavDestination.Companion.findDestinationByRoute
import com.lozanov.viridum.shared.Navigator

@ExperimentalPagerApi
@Composable
fun ViridumApplication(navigator: Navigator, exit: () -> Unit) {
    val navController = rememberNavController()

    ProvideWindowInsets {
        ViridumTheme {
            Scaffold(
                topBar = { AppBar(findDestinationByRoute(
                    navController.currentDestination?.route
                )) },
            ) {
                Column {
                    TabbedNav(navigator = navigator, navBackStackEntry =
                        navController.currentBackStackEntryAsState(),
                        routes = listOf(MainTabs.MODEL_SELECT, MainTabs.AR_SCREEN))
                    NavGraph(navigator, navController, exit = exit)
                }
            }
        }
    }
}

@Composable
private fun AppBar(currentDestination: NavDestination) {
    val title = currentDestination.titleId ?: return

    TopAppBar(title = {
        Text(text = stringResource(title))
    },
        elevation = 0.dp,
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = {
            if(currentDestination.hasBackArrow) {
                Icon(painter =
                    painterResource(R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.back))
            }
        },
        actions = {
            if(currentDestination.hasLogoutAction) {
                IconButton(onClick = {
                    // TODO: Send logout event (ViewModel?? Listener somewhere?)
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
private fun TabbedNav(navigator: Navigator, navBackStackEntry: State<NavBackStackEntry?>, routes: List<MainTabs>) {
    val currentRoute = navBackStackEntry.value?.destination?.route
        ?: MainTabs.MODEL_SELECT.destination.route
    val currentDestination = routes.find { it.destination.route == currentRoute }

    if (currentDestination != null) {
        val pagerState = rememberPagerState(pageCount = routes.size,
            initialPage = currentDestination.ordinal)

        TabRow(selectedTabIndex = pagerState.currentPage, modifier =
                Modifier.navigationBarsHeight(additional = 56.dp), indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
            routes.forEach {
                val tabCurrentlySelected = it.ordinal == currentDestination.ordinal

                Tab(onClick = {
                    if(tabCurrentlySelected) {
                        return@Tab
                    }

                    navigator.navigate(it.destination,
                        navBackStackEntry.value)
                }, text = { val titleRes = it.destination.titleId
                    if(titleRes != null) {
                        Text(text = stringResource(titleRes)) } }, icon = { Icon(
                    painter = painterResource(it.icon), contentDescription = null) },
                        selected = tabCurrentlySelected)
            }

            // TODO: Horizontal Pager declaration somewhere here (or in the nested navigation graph?)
        }
    }
}

private enum class MainTabs(@DrawableRes val icon: Int,
                            val destination: NavDestination) {
    MODEL_SELECT(R.drawable.ic_baseline_file_copy_24, NavDestination.ModelSelection),
    AR_SCREEN(R.string.view_model, NavDestination.ARScreen)
}