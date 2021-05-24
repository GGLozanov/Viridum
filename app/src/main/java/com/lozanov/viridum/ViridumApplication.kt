package com.lozanov.viridum

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.lozanov.viridum.ui.theme.ViridumTheme
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun ViridumApplication() {
    ProvideWindowInsets {
        ViridumTheme {
            Scaffold(
                topBar = { AppBar() }
            ) { innerPaddingModifier ->
                NavGraph(
                )
            }
        }
    }
}

@Composable
private fun AppBar() {

}