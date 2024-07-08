package com.panwar2001.pdfpro.navigation_graphs

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
    navigation(route= Screens.Home.route,
        startDestination= Screens.Home.homeScreen.route) {

        composable(route = Screens.Home.homeScreen.route) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<AppViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()
            val pagerState = rememberPagerState { 2 }
            val context = LocalContext.current
            HomeScreen(
                onNavigationIconClick = { scope.launch { drawerState.apply { if (isClosed) open() else close() } } },
                pdfList = uiState.pdfsList,

                navigateTo = {
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
                },
                query = uiState.query,
                pagerState = pagerState,
                onQueryChange = { viewModel.setSearchText(it) },
                onSearch = {
                    viewModel.setSearchBarActive(false)
                    viewModel.searchPdfs()
                },
                active = uiState.searchBarActive,
                onActiveChange = viewModel::setSearchBarActive,
                loading = false,
                showBottomSheet = uiState.isBottomSheetVisible,
                setBottomSheetState = viewModel::setBottomSheetVisible,
                shareFile = {
                    viewModel.sharePdfFile(it) { intent ->
                        context.startActivity(Intent.createChooser(intent, "Share PDF"))
                    }
                },
                onPdfCardClick = {
                    viewModel.setUri(it)
                    navController.navigate(Screens.Home.PdfViewer.route) {
                        if (drawerState.isOpen) {
                            scope.launch { drawerState.apply { close() } }
                        }
                    }
                },
                options = viewModel.options,
                scrollToPage = {
                    if (it != pagerState.currentPage) scope.launch {
                        viewModel.setSearchBarActive(false)
                        pagerState.animateScrollToPage(
                            it
                        )
                    }
                },
                setSortBy = viewModel::setSortOption,
                toggleSortOrder = viewModel::toggleSortOrder,
                sortBy = uiState.sortOption,
                onSearchTrailingIconClick = {
                    if (uiState.query.isNotEmpty()) {
                        viewModel.setSearchText("")
                    } else if (uiState.searchBarActive) {
                        viewModel.setSearchBarActive(false)
                    }
                }
            )
        }
        composable(route = Screens.Home.LanguagePickerScreen.route) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<AppViewModel>(navController)
            val currentLocale = viewModel.getCurrentLocale()
            LanguagePickerScreen(navigateUp = { navController.navigateUp() },
                viewModel.languages,
                currentLocale = currentLocale,
                setLocale = {
                    viewModel.setLocale(it)
                })
        }
        composable(route = Screens.Home.PdfViewer.route) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<AppViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()
            PdfViewer(
                uri = uiState.pdfUri,
                navigateUp = { navController.navigateUp() },
                uiState.pdfName,
                uiState.numPages
            )
        }
    }
}