package com.lozanov.viridum.viewmodel

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ARScreenViewModel @Inject constructor(
    application: Application,

) : BaseViewModel(application) {
}