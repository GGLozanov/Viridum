package com.lozanov.viridum

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lozanov.viridum.ui.theme.ViridumTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.NavDestination.Companion.findDestinationByRoute
import com.lozanov.viridum.shared.Navigator

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
                    TabbedNav(navigator = navigator, navController = navController,
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
            if(currentDestination.tabDestination) {
                Icon(painter =
                    painterResource(R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.back))
            }
        }
    )
}

@Composable
private fun TabbedNav(navigator: Navigator, navController: NavController, routes: List<MainTabs>) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
        ?: MainTabs.MODEL_SELECT.destination.route
    val currentDestination = routes.find { it.destination.route == currentRoute }

    if (currentDestination != null) {
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
                }, text = { val titleRes = it.destination.titleId
                    if(titleRes != null) {
                        Text(text = stringResource(titleRes)) } }, icon = { Icon(
                    painter = painterResource(it.icon), contentDescription = null) },
                        selected = tabCurrentlySelected)
            }
        }
    }
}

private enum class MainTabs(@DrawableRes val icon: Int,
                            val destination: NavDestination) {
    MODEL_SELECT(R.drawable.ic_baseline_file_copy_24, NavDestination.ModelSelection),
    AR_SCREEN(R.string.view_model, NavDestination.ARScreen)
}