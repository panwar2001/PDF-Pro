package com.panwar2001.pdfpro

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.ui.theme.PDFProTheme
import com.panwar2001.pdfpro.ui.view_models.ProViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.ui.HomeScreen
import com.panwar2001.pdfpro.ui.OnboardScreen
import com.panwar2001.pdfpro.ui.UploadScreen
import com.panwar2001.pdfpro.ui.components.DrawerBody
import com.panwar2001.pdfpro.ui.components.DrawerHeader
import kotlinx.coroutines.launch


class Navigation : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            PDFProTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationController(
                        startDestination = getStartDestination(),
                        setOnboardingFinished = {
                            val sharedPreferences = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isFinished", true)
                            editor.apply()
                        })
                }
            }
        }
    }
    private fun getStartDestination( ): String {
        val sharedPreferences = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val onBoardingIsFinished= sharedPreferences.getBoolean("isFinished", false)
        return if(onBoardingIsFinished) Screens.Home.name else Screens.OnBoard.name
    }
}

/**
 *
 * @author ayush panwar
 * Composable that has navigation host and graph for navigating among different composable screens.
 *
 * @param navController
 */
@Composable
fun NavigationController(
    viewModel: ProViewModel = viewModel(),
    navController: NavHostController= rememberNavController(),
    startDestination:String,
    setOnboardingFinished:()->Unit
)
{
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navigateTo:(String)->Unit={
    navController.navigate(it){
        if(drawerState.isOpen){
            scope.launch {drawerState.apply {close()}}
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
}
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(Modifier.padding(0.dp,0.dp,60.dp,0.dp)) {
                DrawerHeader()
                DrawerBody(items = DataSource.MenuItems){
                    navigateTo(it)
                }
            }
        })
    {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {

            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize(),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            )
            {

                composable(route = Screens.OnBoard.name) {
                    OnboardScreen {
                        // lambda function for navigation
                        setOnboardingFinished()
                        navController.popBackStack()
                        navController.navigate(it)
                    }
                }
                composable(route = Screens.Home.name) {
                    HomeScreen(onNavigationIconClick = {
                        scope.launch {drawerState.apply {if(isClosed) open() else close()}}
                    }) {    // lambda function for navigation
                        navigateTo(it)
                    }
                }
                composable(route = Screens.Upload.name) {
                    UploadScreen(onNavigationIconClick = {
                        scope.launch {drawerState.apply {if(isClosed) open() else close()}}
                    }) { navigateTo(it) }
                }
            }
        }
    }
}

