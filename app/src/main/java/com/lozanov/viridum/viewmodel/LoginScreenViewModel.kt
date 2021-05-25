package com.lozanov.viridum.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.lozanov.viridum.repo.decl.SketchFabRepository
import com.lozanov.viridum.shared.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    navigator: Navigator,
    application: Application,
    sketchFabRepository: SketchFabRepository
) : BaseViewModel(savedStateHandle, navigator, application) {
}