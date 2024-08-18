package com.panwar2001.pdfpro

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.panwar2001.pdfpro.feature.home.homeGraph
import com.panwar2001.pdfpro.feature.onboard.onBoardGraph
import com.panwar2001.pdfpro.feature.settings.settingsScreenNavigation
import com.panwar2001.pdfpro.core.ui.components.AppModalNavigationDrawer
import com.panwar2001.pdfpro.core.ui.components.NavigationActions
import com.panwar2001.pdfpro.core.ui.components.rememberNavActions
import com.panwar2001.pdfpro.feature.img2pdf.img2PdfGraph

/**
 * Composable that has navigation host and graph for navigating
 * among different composable screens.
 */
@Composable
fun NavigationController(
    startDestination:String,
    navActions: NavigationActions = rememberNavActions()
) {
    AppModalNavigationDrawer(
        navActions.drawerState,
        navigateTo = navActions::navigateTo,
        headerImageRes = R.drawable.app_icon
    ) {
        NavHost(navActions.navController, startDestination) {
            onBoardGraph(navActions)
            homeGraph(navActions)
            settingsScreenNavigation(navActions)
            img2PdfGraph(navActions)
//            pdf2txtGraph(navActions)
//            pdf2ImgGraph(navActions)
        }
    }
}

