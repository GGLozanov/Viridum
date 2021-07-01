package com.lozanov.viridum.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.lozanov.viridum.R
import com.lozanov.viridum.shared.ARCoreInstallationWrapper
import com.lozanov.viridum.shared.determineARAvailability
import com.lozanov.viridum.viewmodel.ModelSelectionViewModel

@Composable
fun ModelSelection(
    askedForARCoreAvailability: State<Boolean>,
    viewModel: ModelSelectionViewModel = hiltViewModel()
) {
    ARCoreInstallationWrapper(askedForARCoreAvailability) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = stringResource(R.string.three_dimensional),
                modifier = Modifier.align(Alignment.Center))
        }
    }
    // TODO: Show option for sketchfab select & navigation to file select
}