package com.lozanov.viridum.shared

import android.os.Parcelable
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.toMutableStateList

// initial impl provided by Skydoves
class Navigator<T : Parcelable> private constructor(
    initialBackStack: List<T>,
    onBackPressedDispatch: OnBackPressedDispatcher
) {
    constructor(initialDestination: T,
                onBackPressedDispatch: OnBackPressedDispatcher)
        : this(listOf(initialDestination), onBackPressedDispatch)

    private val backStack = initialBackStack.toMutableStateList()
    private val backCallback = object : OnBackPressedCallback(canGoBack()) {
        override fun handleOnBackPressed() {
            back()
        }
    }.also { callback ->
        onBackPressedDispatch.addCallback(callback)
    }
    val current: T get() = backStack.last()

    fun back() {
        backStack.removeAt(backStack.lastIndex)
        backCallback.isEnabled = canGoBack()
    }

    fun navigate(destination: T) {
        backStack += destination
        backCallback.isEnabled = canGoBack()
    }

    private fun canGoBack(): Boolean = backStack.size > 1
}