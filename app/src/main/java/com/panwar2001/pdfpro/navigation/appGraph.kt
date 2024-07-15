package com.panwar2001.pdfpro.navigation

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.panwar2001.pdfpro.compose.HomeScreen
import com.panwar2001.pdfpro.compose.LanguagePickerScreen
import com.panwar2001.pdfpro.compose.components.PdfViewer
import com.panwar2001.pdfpro.view_models.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.appGraph(scope:CoroutineScope,
                             navActions: NavigationActions){
    navigation(route= Screens.Home.route,
        startDestination= Screens.Home.HomeScreen.route) {

        composable(route = Screens.Home.HomeScreen.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<AppViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val pagerState = rememberPagerState { 2 }
            val context = LocalContext.current
            HomeScreen(
                onNavigationIconClick = navActions::toggleDrawer,
                pdfList = uiState.pdfsList,
                navigateTo = navActions::navigateTo,
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
                    navActions.navigateToScreen(Screens.Home.PdfViewer.route)
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
            val viewModel = navActions.sharedViewModel<AppViewModel>(backStackEntry)
            val currentLocale = viewModel.getCurrentLocale()
            LanguagePickerScreen(navigateUp =navActions::navigateBack,
                viewModel.languages,
                currentLocale = currentLocale,
                setLocale = {
                    viewModel.setLocale(it)
                })
        }
        composable(route = Screens.Home.PdfViewer.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<AppViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            PdfViewer(
                uri = uiState.pdfUri,
                navigateUp = navActions::navigateBack,
                uiState.pdfName,
                uiState.numPages
            )
        }
    }
}