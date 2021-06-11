package com.lozanov.viridum.ui.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.lozanov.viridum.viewmodel.LoginScreenViewModel

@Composable
fun Login(
    onSuccessfulAuth: (token: String) -> Unit,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    // TODO: Use AppAuth for OAuth (in repo)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

    }
}