package com.panwar2001.pdfpro.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.panwar2001.pdfpro.screens.Screens
import com.panwar2001.pdfpro.ui.components.NavigationActions


fun NavGraphBuilder.homeGraph(navActions: NavigationActions){
    navigation(
        route= Screens.Home.route,
        startDestination= Screens.Home.HomeScreen.route
    ) {
        composable(route = Screens.Home.HomeScreen.route) {
            HomeScreen(
                onNavigationIconClick = navActions::openDrawer,
                navigateTo = navActions::navigateTo,
            )
        }
    }
}