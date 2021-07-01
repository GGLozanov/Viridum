package com.lozanov.viridum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.lozanov.viridum.shared.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

abstract class BaseViewModel(
    protected val savedStateHandle: SavedStateHandle,
    val navigator: Navigator,
    application: Application
) : AndroidViewModel(application) {
    // TODO: Fill
}