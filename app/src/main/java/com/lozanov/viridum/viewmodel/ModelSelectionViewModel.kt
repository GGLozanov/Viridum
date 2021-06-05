package com.lozanov.viridum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.lozanov.viridum.shared.Navigator
import javax.inject.Inject

class ModelSelectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    navigator: Navigator,
    application: Application,
) : BaseViewModel(savedStateHandle, navigator, application) {
}