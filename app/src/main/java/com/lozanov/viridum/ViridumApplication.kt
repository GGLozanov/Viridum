package com.lozanov.viridum

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.lozanov.viridum.ui.theme.ViridumTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.Navigator

@Composable
fun ViridumApplication(navigator: Navigator, exit: () -> Unit) {
    val navController = rememberNavController()

    ProvideWindowInsets {
        ViridumTheme {
            Scaffold(
                topBar = { AppBar(NavDestination.findDestinationByRoute(
                    navController.currentDestination?.route
                )) }
            ) {
                NavGraph(navigator, navController, exit = exit)
            }
        }
    }
}

@Composable
private fun AppBar(currentDestination: NavDestination) {
    val title = currentDestination.title ?: return

    TopAppBar(title = {
        Text(text = title)
    })
}