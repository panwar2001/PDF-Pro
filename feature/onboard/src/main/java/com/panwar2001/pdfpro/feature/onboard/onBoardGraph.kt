package com.panwar2001.pdfpro.feature.onboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.panwar2001.pdfpro.screens.Screens
import com.panwar2001.pdfpro.ui.components.NavigationActions
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.onBoardGraph(navActions: NavigationActions,
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
            navActions.navigateTo(Screens.Home.route)
            setOnboardingFinished()
        },
            pagerState = pagerState,
            onBoardList = viewModel.onBoardList,
            onNextButtonClick = {
                if (pagerState.currentPage + 1 == pagerState.pageCount) {
                    navActions.navigateTo(Screens.Home.route)
                    setOnboardingFinished()
                } else {
                    navActions.scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            })
    }
}