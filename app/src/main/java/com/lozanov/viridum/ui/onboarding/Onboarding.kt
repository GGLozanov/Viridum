package com.lozanov.viridum.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun Onboarding(onboardingComplete: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = PageData.PAGE_COUNT)

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),) { page ->
            OnboardingSegment(pagerState, onboardingComplete, PageData
                .computePageInstanceForPageIndex(page))
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )
    }
}

@ExperimentalPagerApi
@Composable
fun OnboardingSegment(
    pagerState: PagerState,
    onboardingComplete: () -> Unit,
    pageData: PageData
) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
            .fillMaxSize()) {
        Text(text = stringResource(pageData.headerId),
            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Image(painter = painterResource(id = pageData.drawableId),
            contentDescription = null)
        Text(text = stringResource(pageData.subtitleId),
            textAlign = TextAlign.Center, modifier = Modifier.alpha(0.8f))
        TextButton(onClick = {
            if(pageData.lastSegment) {
                onboardingComplete()
            } else {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }) {
            Text(text = stringResource(if(pageData.lastSegment)
                com.lozanov.viridum.R.string.finish else
                    com.lozanov.viridum.R.string.text_continue))
        }
    }
}

data class PageData(
    val headerId: Int,
    @DrawableRes val drawableId: Int,
    val subtitleId: Int,
    val lastSegment: Boolean = false
) {
    companion object {
        const val PAGE_COUNT = 2

        fun computePageInstanceForPageIndex(page: Int): PageData {
            val headerId: Int
            val drawableId: Int
            val subtitleId: Int

            when(page) {
                0 -> {
                    headerId = com.lozanov.viridum.R.string.welcome_viridum
                    drawableId = com.lozanov.viridum.R.drawable.logo
                    subtitleId = com.lozanov.viridum.R.string.onboarding_subtitle_1
                }
                1 -> {
                    headerId = com.lozanov.viridum.R.string.how_to_3d
                    drawableId = com.lozanov.viridum.R.drawable.ic_baseline_3d_rotation_24
                    subtitleId = com.lozanov.viridum.R.string.onboarding_subtitle_2
                }
                else -> throw IndexOutOfBoundsException("Page index out of bounds for desired data!")
            }

            return PageData(headerId, drawableId, subtitleId,
                lastSegment = page == PAGE_COUNT - 1)
        }
    }
}