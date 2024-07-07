package com.panwar2001.pdfpro.navigation_graphs

import android.content.ContentUris
import android.content.Intent
import android.provider.MediaStore
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.sharedViewModel
import com.panwar2001.pdfpro.ui.HomeScreen
import com.panwar2001.pdfpro.ui.LanguagePickerScreen
import com.panwar2001.pdfpro.ui.components.PdfViewer
import com.panwar2001.pdfpro.ui.view_models.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.appGraph(navController: NavController,
                             scope:CoroutineScope,
                             drawerState: DrawerState){

    composable(route = Screens.Home.route) {backStackEntry->
        val viewModel = backStackEntry.sharedViewModel<AppViewModel>(navController)
        val uiState by viewModel.uiState.collectAsState()
        val pagerState=rememberPagerState{2}
        val context= LocalContext.current
        HomeScreen(
            onNavigationIconClick = {scope.launch { drawerState.apply { if (isClosed) open() else close() } }},
            pdfList = uiState.pdfsList,
            navigateTo ={navController.navigate(it) {
                if (drawerState.isOpen) {
                    scope.launch { drawerState.apply { close() } }
                }
                // Avoid multiple copies of the same destination when
                // selecting the same item
                launchSingleTop = true
                // Restore state when selecting a previously selected item
                restoreState = true
            }},
            query = uiState.query,
            pagerState = pagerState,
            onQueryChange = {viewModel.setSearchText(it)},
            onSearch = { viewModel.setSearchBarActive(false)
                         viewModel.searchPdfs()},
            active = uiState.searchBarActive,
            onActiveChange = viewModel::setSearchBarActive,
            loading = false,
            showBottomSheet = uiState.isBottomSheetVisible,
            setBottomSheetState = viewModel::setBottomSheetVisible,
            shareFile = {
                viewModel.sharePdfFile(it) {intent->
                    context.startActivity(Intent.createChooser(intent, "Share PDF"))
                }
            },
            onPdfCardClick = {
                viewModel.setUri(it)
                navController.navigate(Screens.PdfViewer.route) {
                    if (drawerState.isOpen) {
                        scope.launch { drawerState.apply { close() } }
                    }}
            },
            options = viewModel.options,
            scrollToPage = {if(it!=pagerState.currentPage)scope.launch { pagerState.scrollToPage(it)}},
            setSortBy = viewModel::setSortOption,
            toggleSortOrder = viewModel::toggleSortOrder,
            sortBy = uiState.sortOption
        )
    }
    composable(route= Screens.LanguagePickerScreen.route){backStackEntry->
        val viewModel = backStackEntry.sharedViewModel<AppViewModel>(navController)
        val currentLocale=viewModel.getCurrentLocale()
        val languages= listOf(
            stringArrayResource(id = R.array.english),
            stringArrayResource(id = R.array.French),
            stringArrayResource(id = R.array.Japanese),
            stringArrayResource(id = R.array.Russian),
            stringArrayResource(id = R.array.hindi)
        )
        LanguagePickerScreen(navigateUp={navController.navigateUp()},
            languages,
            currentLocale =currentLocale,
            setLocale = {
              viewModel.setLocale(it)
            })
    }
    composable(route=Screens.PdfViewer.route){backStackEntry->
        val viewModel = backStackEntry.sharedViewModel<AppViewModel>(navController)
        val uiState by viewModel.uiState.collectAsState()
        val uri=uiState.pdfUri
        Text(text = uiState.pdfName)
//        if(uri!= null) {
//            PdfViewer(uri = uri,
//                navigateUp ={navController.navigateUp()},
//                uiState.pdfName,
//                uiState.numPages)
//
//        }
    }
}