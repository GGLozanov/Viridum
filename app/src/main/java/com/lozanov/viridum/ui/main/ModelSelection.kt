package com.lozanov.viridum.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lozanov.viridum.MainActivity
import com.lozanov.viridum.R
import com.lozanov.viridum.shared.ARCoreInstallationWrapper
import com.lozanov.viridum.shared.determineARAvailability
import com.lozanov.viridum.shared.getActivity
import com.lozanov.viridum.viewmodel.ModelSelectionViewModel
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat


@Composable
fun ModelSelection(
    askedForARCoreAvailability: State<Boolean>,
    viewModel: ModelSelectionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context.getActivity() as MainActivity

    val chosenFile = activity.viewModel
        .currentSelectedModelUri.collectAsState().value

    ARCoreInstallationWrapper(askedForARCoreAvailability) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = stringResource(R.string.three_dimensional),
                modifier = Modifier.align(Alignment.Center))

            ExtendedFloatingActionButton(onClick = {
                val fileChooseIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "*/*"
                    Intent.createChooser(this,
                        context.resources.getString(R.string.choose_file))
                }

                startActivityForResult(activity,
                    fileChooseIntent, MainActivity.FILE_PICK_REQ_CODE, null)
            }, icon = {
                Icon(painter =
                    painterResource(R.drawable.ic_baseline_file_present_24),
                    contentDescription = stringResource(R.string.back)) },
            text = {
                Text(text = stringResource(R.string.choose_file_hint),
                    modifier = Modifier.padding(3.dp))
            },
                modifier = Modifier.padding(8.dp))

            val path = chosenFile?.path
            if(path != null) {
                Text(text = path,
                    modifier = Modifier.align(Alignment.Center))
            }
        }
    }
    // TODO: Show option for sketchfab select & navigation to file select
}