package com.lozanov.viridum.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.lozanov.viridum.shared.NavAnimationSlide
import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.Navigator

@ExperimentalAnimationApi
@ExperimentalPagerApi
fun NavGraphBuilder.main(
    askedForARCoreAvailability: State<Boolean>,
    navigator: Navigator,
) {
    // TODO: Use onLogout
    composable(NavDestination.MainDestination.ModelSelection.route) {
        NavAnimationSlide(navigator = navigator) {
            ModelSelection(
                askedForARCoreAvailability = askedForARCoreAvailability
            )
        }
    }
    composable(NavDestination.MainDestination.ARScreen.route) {
        NavAnimationSlide(navigator = navigator) {
            ARScreen()
        }
    }
}

// TODO: If implementing tabbed w/ pager
//val pagerState = tabRowPagerState.value
//if(pagerState != null) {
//} else {
//    // TODO: Possible?
//}