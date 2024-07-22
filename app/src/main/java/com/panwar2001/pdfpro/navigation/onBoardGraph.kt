package com.panwar2001.pdfpro.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.panwar2001.pdfpro.compose.OnboardScreen
import com.panwar2001.pdfpro.view_models.OnBoardScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.onBoardGraph(scope: CoroutineScope,
                                 navActions: NavigationActions,
                                 setOnboardingFinished:()->Unit ) {
    composable(route = Screens.OnBoard.route) {
        val viewModel = OnBoardScreenViewModel()
        val pagerState = rememberPagerState(pageCount = { viewModel.onBoardList.size })

        /**
         * TODO
         * Solve issue : while navigation from onboarding screen to home screen
         * the screen freezes and not loads
         */
        OnboardScreen(navigateToHome = {
            navActions.navigateToHome()
            setOnboardingFinished()
        },
            pagerState = pagerState,
            onBoardList = viewModel.onBoardList,
            onNextButtonClick = {
                if (pagerState.currentPage + 1 == pagerState.pageCount) {
                    navActions.navigateToHome()
                    setOnboardingFinished()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            })
    }
}

