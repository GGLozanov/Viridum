package com.lozanov.viridum

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.lozanov.viridum.ui.theme.ViridumTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.lozanov.viridum.shared.Navigator

@Composable
fun ViridumApplication(navigator: Navigator, exit: () -> Unit) {
    ProvideWindowInsets {
        ViridumTheme {
            Scaffold(
                topBar = { AppBar() }
            ) { innerPaddingModifier ->
                NavGraph(navigator) {

                }
            }
        }
    }
}

@Composable
private fun AppBar() {

}