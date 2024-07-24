package com.panwar2001.pdfpro.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberNavActions(
    navController: NavHostController= rememberNavController(),
    scope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
): NavigationActions{
    return remember(navController,scope,drawerState) {
        NavigationActions(navController,scope,drawerState)
    }
}
class NavigationActions(val navController: NavHostController,
                        val scope: CoroutineScope,
                        val drawerState: DrawerState){
    fun navigateToHome()= navController.navigate(Screens.Home.route) {
                   navController.popBackStack()
                }
    fun navigateTo(screen:String) = navController.navigate(screen){
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // selecting the same item
            launchSingleTop = true
            // Restore state when selecting a previously selected item
            restoreState = true
        }
    fun navigateToScreen(screen: String)=navController.navigate(screen)

    fun navigateBack()=  navController.navigateUp()
    fun openDrawer()=scope.launch {
        drawerState.apply { open() }
    }

    /**
     *  create sharedViewModel Instance with it with navBackStackEntry.
     */
    @Composable
    inline fun < reified T: ViewModel> sharedViewModel(backStackEntry: NavBackStackEntry): T {
        val navGraphRoute = backStackEntry.destination.parent?.route ?: return hiltViewModel()
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(navGraphRoute)
        }
        return hiltViewModel(parentEntry)
    }
}