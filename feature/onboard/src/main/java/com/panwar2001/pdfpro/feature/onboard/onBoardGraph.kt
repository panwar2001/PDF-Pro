package com.panwar2001.pdfpro.feature.onboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.panwar2001.pdfpro.model.OnBoardData
import com.panwar2001.pdfpro.onboard.R
import com.panwar2001.pdfpro.screens.Screens
import com.panwar2001.pdfpro.core.ui.components.NavigationActions
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.onBoardGraph(navActions: NavigationActions) {

    composable(route = Screens.OnBoard.route) {
        val viewModel: OnBoardScreenViewModel = hiltViewModel()
        val pagerState = rememberPagerState(pageCount = { onBoardList.size })

        /**
         * TODO
         * Solve issue : while navigation from onboarding screen to home screen
         * the screen freezes and not loads
         */
        OnboardScreen(navigateToHome = {
            navActions.navigateTo(Screens.Home.route)
            viewModel.setOnboardingFinished()
        },
            pagerState = pagerState,
            onBoardList =  onBoardList ,
            onNextButtonClick = {
                if (pagerState.currentPage + 1 == pagerState.pageCount) {
                    navActions.navigateTo(Screens.Home.route)
                    viewModel.setOnboardingFinished()
                } else {
                    navActions.scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            })
    }
}


internal val onBoardList= listOf(
//    OnBoardData(icon= R.drawable.ocr,
//        title= R.string.pdf2text,
//        description = R.string.pdf2txt_description),
    OnBoardData(icon= R.drawable.pdf_svg,
        title= R.string.img2pdf,
        description = R.string.img2pdf_description)
)
