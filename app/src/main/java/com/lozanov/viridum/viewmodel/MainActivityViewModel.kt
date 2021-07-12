package com.lozanov.viridum.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.lozanov.viridum.shared.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    navigator: Navigator,
    application: Application,
) : BaseViewModel(savedStateHandle, navigator, application) {
    private var _currentSelectedModelUri: MutableStateFlow<Uri?> = MutableStateFlow(null)
    val currentSelectedModelUri: StateFlow<Uri?> get() = _currentSelectedModelUri

    fun setCurrentSelectedModelUri(model: Uri?) {
        _currentSelectedModelUri.value = model
    }
}