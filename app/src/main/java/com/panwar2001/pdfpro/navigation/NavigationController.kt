package com.panwar2001.pdfpro.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.panwar2001.pdfpro.compose.OnboardScreen
import com.panwar2001.pdfpro.compose.components.AppModalNavigationDrawer
import com.panwar2001.pdfpro.view_models.OnBoardScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 *
 * @author ayush panwar
 * Composable that has navigation host and graph for navigating among different composable screens.
 *
 * @param navController
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationController(
    navController: NavHostController = rememberNavController(),
    startDestination:String,
    setOnboardingFinished:()->Unit,
    setTheme:(Boolean)->Unit,
    currentTheme:Boolean,
    scope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navActions: NavigationActions = remember(navController,drawerState) {
                           NavigationActions(navController,drawerState,scope)}
    ) {
    AppModalNavigationDrawer(
        drawerState = drawerState,
        setTheme = setTheme ,
        currentTheme = currentTheme ,
        navigateTo = navActions::navigateTo) {

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ){
            composable(route = Screens.OnBoard.route) {
                val viewModel= OnBoardScreenViewModel()
                val isLoading  by viewModel.loading.collectAsState()
                val pagerState = rememberPagerState( pageCount = { viewModel.onBoardList.size})

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
            appGraph(scope=scope,
                navActions=navActions)

            pdf2txtGraph(scope=scope,
                navActions=navActions)

            pdf2ImgGraph(scope=scope,
                navActions=navActions)

            img2PdfGraph(scope=scope,
                navActions=navActions)
        }
    }
}

