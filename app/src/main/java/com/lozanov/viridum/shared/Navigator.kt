package com.lozanov.viridum.shared

import android.os.Parcelable
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.lang.IllegalStateException

class Navigator(
    private val destinationsExitOnPop: List<NavDestination>? = null
) {
    private var currentlyNav: Boolean? = null
    private val _latestNavigateDestination =
        MutableStateFlow<NavDestination>(NavDestination.Root)

    val latestNavigationState: Flow<LatestNavigationState>
        get() =
        _latestNavigateDestination.map {
            when {
                it == NavDestination.Root
                        && _latestNavigateDestination.replayCache.size <= 1 -> { // second condition guards against initial popping (may not work?)
                    LatestNavigationState.Exit
                }
                currentlyNav == true -> {
                    LatestNavigationState.Navigate(it)
                }
                currentlyNav == false -> {
                    LatestNavigationState.Pop(it)
                }
                else -> {
                    LatestNavigationState.Idle
                }
            }
        }

    fun navigate(dest: NavDestination) {
        currentlyNav = true
        _latestNavigateDestination.value = dest
    }

    fun pop(until: NavDestination? = null) {
        currentlyNav = false

        if(!_latestNavigateDestination.replayCache.contains(until)) {
            throw IllegalStateException("Cannot pop destination not contained in navDestination replay cache " +
                    "(i.e. previous destinations)!")
        }

        val backstackPopUntil = _latestNavigateDestination.replayCache
                .subList(_latestNavigateDestination.replayCache
                    .indexOf(until), _latestNavigateDestination.replayCache.lastIndex)

        if(destinationsExitOnPop?.any { backstackPopUntil.contains(it) } == true) { // pop destination met on backstack
            _latestNavigateDestination.value = NavDestination.Root
            return
        }

        if(until == null) {
            _latestNavigateDestination.value =
                _latestNavigateDestination.replayCache[
                        _latestNavigateDestination.replayCache.lastIndex - 1]
        } else {
            _latestNavigateDestination.value = until
        }
    }

    sealed class LatestNavigationState {
        data class Navigate(val dest: NavDestination) : LatestNavigationState()

        data class Pop(val until: NavDestination? = null) : LatestNavigationState()

        object Exit : LatestNavigationState()

        object Idle : LatestNavigationState()
    }
}