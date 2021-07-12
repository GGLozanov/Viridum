package com.lozanov.viridum

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyCharacterMap
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.ar.core.ArCoreApk
import com.lozanov.viridum.shared.Navigator
import com.lozanov.viridum.ui.theme.ViridumTheme
import com.lozanov.viridum.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import java.net.URI
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: MainActivityViewModel by viewModels()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == FILE_PICK_REQ_CODE && data != null) {
            viewModel.setCurrentSelectedModelUri(data.data)
        }
    }

    companion object {
        const val FILE_PICK_REQ_CODE = 244
        
        const val URI_TAG = "MODEL_FILE_URI"
    }
}