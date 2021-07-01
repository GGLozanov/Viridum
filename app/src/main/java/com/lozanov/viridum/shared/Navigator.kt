package com.lozanov.viridum.shared

import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.*
import java.lang.IllegalStateException

class Navigator(
    private val destinationsExitOnPop: List<NavDestination>? = null
) {
    private var _currentlyNav: Boolean? = null
    val currentlyNav: Boolean get() = _currentlyNav ?: true

    private val _latestNavigateDestination =
        MutableSharedFlow<NavDestination>(replay = MAX_NAV_DESTINATIONS_REPLAY).apply {
            tryEmit(NavDestination.Root)
        }

    val latestNavigationState: Flow<LatestNavigationState>
        get() =
        _latestNavigateDestination.map {
            when {
                it == NavDestination.Root
                        && _latestNavigateDestination.replayCache.isNotEmpty() -> { // second condition guards against initial popping (may not work?)
                    LatestNavigationState.Exit
                }
                _currentlyNav == true -> {
                    LatestNavigationState.Navigate(it)
                }
                _currentlyNav == false -> {
                    LatestNavigationState.Pop(it)
                }
                else -> {
                    LatestNavigationState.Idle
                }
            }
        }

    fun navigate(dest: NavDestination,
                 backStackEntry: NavBackStackEntry? = null) {
        val performNavigation = {
            _latestNavigateDestination.tryEmit(dest)
            _currentlyNav = true
        }

        if(backStackEntry != null) {
            if(backStackEntry.lifecycleIsResumed())  {
                performNavigation()
            }
        } else {
            performNavigation()
        }
    }

    // TODO: Fix popping behaviour (this DOES NOT work like a normal backstack)
    // ex replay cache: A -> B
    // pop B
    // A -> B -> A
    // pop A: A -> B -> A -> B (Dumb as fuck???)
    suspend fun pop(until: NavDestination? = null) {
        if(!_latestNavigateDestination.replayCache.contains(until)) {
            throw IllegalStateException("Cannot pop destination not contained in navDestination replay cache " +
                    "(i.e. previous destinations)!")
        }

        if(until == null) {
            val isLastDestinationNotRoot = if(destinationsExitOnPop != null && destinationsExitOnPop.isNotEmpty()) {
                !destinationsExitOnPop.contains(
                    _latestNavigateDestination.lastOrNull() ?: destinationsExitOnPop[0])
            } else false

            val rootOfLast = if(_latestNavigateDestination.replayCache.lastIndex > 0 &&
                    isLastDestinationNotRoot) {
                val lastDestination = _latestNavigateDestination.replayCache[
                        _latestNavigateDestination.replayCache.lastIndex - 1]
                _latestNavigateDestination.replayCache[
                        _latestNavigateDestination.replayCache.indexOfFirst { it == lastDestination } - 1]
            } else {
                NavDestination.Root
            }

            _latestNavigateDestination.tryEmit(rootOfLast)
        } else {
            val backstackPopUntil = _latestNavigateDestination.replayCache
                .subList(_latestNavigateDestination.replayCache
                    .indexOf(until), _latestNavigateDestination.replayCache.lastIndex)

            if(destinationsExitOnPop?.any { backstackPopUntil.contains(it) } == true) { // pop destination met on backstack
                _latestNavigateDestination.tryEmit(NavDestination.Root)
                return
            }

            _latestNavigateDestination.tryEmit(until)
        }

        _currentlyNav = false
    }

    sealed class LatestNavigationState {
        data class Navigate(val dest: NavDestination) : LatestNavigationState()

        data class Pop(val until: NavDestination? = null) : LatestNavigationState()

        object Exit : LatestNavigationState()

        object Idle : LatestNavigationState()
    }

    companion object {
        private const val MAX_NAV_DESTINATIONS_REPLAY = 50
    }
}