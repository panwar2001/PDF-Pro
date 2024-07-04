package com.panwar2001.pdfpro.navigation_graphs

import android.content.ContentUris
import android.provider.MediaStore
import androidx.compose.material3.DrawerState
import androidx.compose.ui.res.stringArrayResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.sharedViewModel
import com.panwar2001.pdfpro.ui.HomeScreen
import com.panwar2001.pdfpro.ui.LanguagePickerScreen
import com.panwar2001.pdfpro.ui.components.PdfViewer
import com.panwar2001.pdfpro.ui.view_models.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun NavGraphBuilder.appGraph(navController: NavController,
                             scope:CoroutineScope,
                             drawerState: DrawerState){

    composable(route = Screens.Home.route) {
        HomeScreen(onNavigationIconClick = {
            scope.launch { drawerState.apply { if (isClosed) open() else close() } }
        }) {
            // lambda function for navigation
            navController.navigate(it) {
                if (drawerState.isOpen) {
                    scope.launch { drawerState.apply { close() } }
                }
                // Avoid multiple copies of the same destination when
                // selecting the same item
                launchSingleTop = true
                // Restore state when selecting a previously selected item
                restoreState = true
            }
        }
    }
    composable(route= Screens.LanguagePickerScreen.route){model->
        val viewModel = model.sharedViewModel<AppViewModel>(navController)
        val languages= listOf(
            stringArrayResource(id = R.array.english),
            stringArrayResource(id = R.array.French),
            stringArrayResource(id = R.array.Japanese),
            stringArrayResource(id = R.array.Russian),
            stringArrayResource(id = R.array.hindi)
        )
        val currentLocale=viewModel.getCurrentLocale()
        LanguagePickerScreen(navigateUp={navController.navigateUp()},
            languages,
            currentLocale =currentLocale,
            setLocale = {
              viewModel.setLocale(it)
            })
    }
    composable(route=Screens.PdfViewer.route+"/{uriID}",
        arguments = listOf(navArgument("uriID") { type = NavType.LongType })){ backStackEntry ->
        val id=backStackEntry.arguments?.getLong("uriID")
        if(id!=null) {
            val baseUri = MediaStore.Files.getContentUri("external")
            val uri = ContentUris.withAppendedId(baseUri, id)
            PdfViewer(uri = uri,
                navigateUp ={navController.navigateUp()},
                "",
                20)

        }else{
            navController.navigateUp()
        }
    }
}