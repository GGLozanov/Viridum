package com.lozanov.viridum.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.lozanov.viridum.shared.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ARViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    navigator: Navigator,
    application: Application,
) : BaseViewModel(savedStateHandle, navigator, application) {
    // TODO: Collect selected URIs for models from Room DB? Preserve state always, even after app close?
}