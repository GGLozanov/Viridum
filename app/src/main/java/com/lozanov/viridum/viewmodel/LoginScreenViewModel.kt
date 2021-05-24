package com.lozanov.viridum.viewmodel

import android.app.Application
import com.lozanov.viridum.repo.decl.SketchFabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    application: Application,
    sketchFabRepository: SketchFabRepository
) : BaseViewModel(application) {
}