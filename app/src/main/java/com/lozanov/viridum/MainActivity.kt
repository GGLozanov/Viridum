package com.lozanov.viridum

import android.os.Bundle
import android.util.Log
import android.view.KeyCharacterMap
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.ar.core.ArCoreApk
import com.lozanov.viridum.shared.Navigator
import com.lozanov.viridum.ui.theme.ViridumTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var navigator: Navigator

    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Sketchfab API integration + auth and custom 3d model imports from ContentProvider
        // TODO: For use in AR?

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