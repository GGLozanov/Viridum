package com.lozanov.viridum.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.lozanov.viridum.shared.NavDestination

@ExperimentalPagerApi
fun NavGraphBuilder.main(
    askedForARCoreAvailability: State<Boolean>,
    onLogout: () -> Unit,
) {
    // TODO: Use onLogout
    composable(NavDestination.MainDestination.ModelSelection.route) {
        ModelSelection(
            askedForARCoreAvailability = askedForARCoreAvailability
        )
    }
    composable(NavDestination.MainDestination.ARScreen.route) {
        ARScreen()
    }
}

// TODO: If implementing tabbed w/ pager
//val pagerState = tabRowPagerState.value
//if(pagerState != null) {
//} else {
//    // TODO: Possible?
//}