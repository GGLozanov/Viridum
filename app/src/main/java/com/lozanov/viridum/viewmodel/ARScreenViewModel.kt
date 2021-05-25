package com.lozanov.viridum.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.lozanov.viridum.shared.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ARScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    navigator: Navigator,
    application: Application,
) : BaseViewModel(savedStateHandle, navigator, application) {
}