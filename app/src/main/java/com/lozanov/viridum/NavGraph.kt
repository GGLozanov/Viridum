package com.lozanov.viridum

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.lozanov.viridum.shared.NavDestination

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {

    // TODO: Read sharedprefs for login state & onboarding and change destination accordingly
    val initialDestination: NavDestination = NavDestination.Login

    NavHost(navController = navController,
        startDestination = initialDestination.route) {
        // TODO: Add composables
    }
}

// NavComponent actions bundled
private class Actions {

}