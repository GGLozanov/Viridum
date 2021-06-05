package com.lozanov.viridum.ui.auth

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.lozanov.viridum.viewmodel.LoginScreenViewModel

@Composable
fun Login(
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    // TODO: Use AppAuth for OAuth (in repo)
}