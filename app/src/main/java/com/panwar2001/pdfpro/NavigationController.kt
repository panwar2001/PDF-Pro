package com.panwar2001.pdfpro

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.panwar2001.pdfpro.feature.home.homeGraph
import com.panwar2001.pdfpro.feature.onboard.onBoardGraph
import com.panwar2001.pdfpro.ui.components.AppModalNavigationDrawer
import com.panwar2001.pdfpro.ui.components.NavigationActions
import com.panwar2001.pdfpro.ui.components.rememberNavActions

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
        headerImageRes = R.drawable.pdf_svg
    ) {
        NavHost(navActions.navController, startDestination) {
            onBoardGraph(navActions)
            homeGraph(navActions)




//            pdf2txtGraph(navActions)
//            pdf2ImgGraph(navActions)
//            img2PdfGraph(navActions)
        }
    }
}

