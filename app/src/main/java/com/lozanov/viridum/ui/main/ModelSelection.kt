package com.lozanov.viridum.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.lozanov.viridum.shared.ARCoreInstallationWrapper
import com.lozanov.viridum.shared.determineARAvailability
import com.lozanov.viridum.viewmodel.ModelSelectionViewModel

@Composable
fun ModelSelection(
    askedForARCoreAvailability: State<Boolean>,
    viewModel: ModelSelectionViewModel = hiltViewModel()
) {
    ARCoreInstallationWrapper(askedForARCoreAvailability) {

    }
    // TODO: Show option for sketchfab select & navigation to file select
}