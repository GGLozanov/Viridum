package com.lozanov.viridum.shared

import android.util.Log
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
                        && _latestNavigateDestination.replayCache.size > 1 -> { // second condition guards against initial popping (may not work?)
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
            Log.i("Navigator", "navigate -> dest: $dest")
            _currentlyNav = true
            _latestNavigateDestination.tryEmit(dest)
        }

        if(backStackEntry != null) {
            if(backStackEntry.lifecycleIsResumed())  {
                performNavigation()
            }
        } else {
            performNavigation()
        }
    }

    // TODO: Add exemption from special popping behaviour for tabbed navigation?
    // TODO: Since it has to keep history, make it so that it DOES take last index - 1 because of tab history
    suspend fun pop(rawPop: Boolean = false, until: NavDestination? = null) {
        Log.i("Navigator",
            "pop -> _latestNavigateDestination replay: ${_latestNavigateDestination.replayCache}; until: $until")
        if(until != null &&
                !_latestNavigateDestination.replayCache.contains(until)) {
            throw IllegalStateException("Cannot pop destination not contained in navDestination replay cache " +
                    "(i.e. previous destinations)!")
        }

        _currentlyNav = false

        if(until == null) {
            val isReplayCachePoppable = _latestNavigateDestination.replayCache.lastIndex > 0

            if(rawPop) {
                if(isReplayCachePoppable) {
                    _latestNavigateDestination.tryEmit(_latestNavigateDestination.replayCache[
                            _latestNavigateDestination.replayCache.lastIndex - 1])
                }
                return
            }

            val isLastDestinationNotRoot = if(destinationsExitOnPop != null && destinationsExitOnPop.isNotEmpty()) {
                !destinationsExitOnPop.contains(
                    _latestNavigateDestination.lastOrNull() ?: destinationsExitOnPop[0])
            } else false

            Log.i("Navigator",
                "pop -> isLastDestinationNotRoot: $isLastDestinationNotRoot")

            val rootOfLast = if(isReplayCachePoppable &&
                    isLastDestinationNotRoot) {
                val lastDestination = _latestNavigateDestination.replayCache[
                        _latestNavigateDestination.replayCache.lastIndex - 1]
                _latestNavigateDestination.replayCache[
                        _latestNavigateDestination.replayCache.indexOfFirst { it == lastDestination } - 1]
            } else {
                NavDestination.Root
            }

            Log.i("Navigator",
                "pop -> isLastDestinationNotRoot: $isLastDestinationNotRoot; rootOfLast: $rootOfLast")

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