package com.panwar2001.pdfpro.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NavigationActions(val navController: NavController,
                        private val drawerState: DrawerState,
                        private val scope: CoroutineScope){
    fun navigateToHome()= navController.navigate(Screens.Home.route) {
                   navController.popBackStack()
                }
    fun navigateTo(screen:String) = navController.navigate(screen){
            if (drawerState.isOpen) {
                scope.launch { drawerState.apply { close() } }
            }
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
    fun navigateToScreen(screen: String)=navController.navigate(screen){
        closeDrawer()
    }
   private fun closeDrawer(){
        if (drawerState.isOpen) {
            scope.launch { drawerState.apply { close() } }
        }
    }
    fun toggleDrawer()=scope.launch {
        drawerState.apply {
            if (isClosed) open() else close()
        }
    }

    fun navigateBack()=  navController.navigateUp()

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