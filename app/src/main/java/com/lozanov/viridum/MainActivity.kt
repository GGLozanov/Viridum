package com.lozanov.viridum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lozanov.viridum.shared.Navigator
import com.lozanov.viridum.ui.theme.ViridumTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Check ARCore availability & unlock Camera permissions at proper screen
        // TODO: Sketchfab API integration + auth and custom 3d model imports from ContentProvider
        // TODO: For use in AR?

        // TODO: Navigation

        setContent {
            ViridumTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ViridumApplication(navigator) {
                        finish()
                    }
                }
            }
        }
    }
}