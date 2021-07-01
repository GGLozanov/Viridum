package com.lozanov.viridum.shared

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment

@ExperimentalAnimationApi
@Composable
fun NavAnimationSlide(navigator: Navigator, content: @Composable AnimatedVisibilityScope.() -> Unit) {
    AnimatedVisibility(
        visible = navigator.currentlyNav, // navigate/pop toggle
        enter = slideInVertically(
            initialOffsetY = { -40 }
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        content = content
    )
}

@ExperimentalAnimationApi
@Composable
fun NavAnimationFade(navigator: Navigator, content: @Composable AnimatedVisibilityScope.() -> Unit) {
    AnimatedVisibility(
        visible = navigator.currentlyNav,
        enter = expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = shrinkVertically() + fadeOut(),
        content = content
    )
}