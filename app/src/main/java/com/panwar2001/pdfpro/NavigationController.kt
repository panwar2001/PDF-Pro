package com.panwar2001.pdfpro.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.panwar2001.pdfpro.ui.components.AppModalNavigationDrawer

/**
 * Composable that has navigation host and graph for navigating among different composable screens.
 */
@Composable
fun NavigationController(
    startDestination:String,
    setOnboardingFinished:()->Unit,
    setTheme:(Boolean)->Unit,
    currentTheme:Boolean,
    navActions: NavigationActions = rememberNavActions()
) {
    com.panwar2001.pdfpro.ui.components.AppModalNavigationDrawer(
        navActions.drawerState,
        setTheme,
        currentTheme,
        navigateTo = navActions::navigateTo
    ) {
        NavHost(navActions.navController, startDestination) {
            onBoardGraph(navActions, setOnboardingFinished)
            appGraph(navActions)
            pdf2txtGraph(navActions)
            pdf2ImgGraph(navActions)
            img2PdfGraph(navActions)
        }
    }
    }

