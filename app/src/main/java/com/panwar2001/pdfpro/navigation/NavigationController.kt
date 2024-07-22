package com.panwar2001.pdfpro.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.panwar2001.pdfpro.compose.components.AppModalNavigationDrawer
import kotlinx.coroutines.CoroutineScope

/**
 *
 * Composable that has navigation host and graph for navigating among different composable screens.
 *
 * @param navController
 */
@Composable
fun NavigationController(
    startDestination:String,
    setOnboardingFinished:()->Unit,
    setTheme:(Boolean)->Unit,
    currentTheme:Boolean,
    navController: NavHostController = rememberNavController(),
    scope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navActions: NavigationActions = remember(navController,drawerState) {
                           NavigationActions(navController,drawerState,scope)}
) {
    AppModalNavigationDrawer(
        drawerState = drawerState,
        setTheme = setTheme,
        currentTheme = currentTheme,
        navigateTo = navActions::navigateTo
    ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize(),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                onBoardGraph(
                    scope = scope,
                    navActions = navActions,
                    setOnboardingFinished = setOnboardingFinished
                )
                appGraph(
                    scope = scope,
                    navActions = navActions
                )

                pdf2txtGraph(
                    scope = scope,
                    navActions = navActions
                )

                pdf2ImgGraph(
                    scope = scope,
                    navActions = navActions
                )

                img2PdfGraph(
                    scope = scope,
                    navActions = navActions
                )
            }
        }
    }