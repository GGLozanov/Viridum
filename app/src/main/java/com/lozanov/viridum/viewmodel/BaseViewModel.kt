package com.lozanov.viridum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
abstract class BaseViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    // TODO: Fill
}