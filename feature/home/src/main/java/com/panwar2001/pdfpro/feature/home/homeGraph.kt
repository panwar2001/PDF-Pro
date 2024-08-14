package com.panwar2001.pdfpro.feature.home

import android.content.ContentUris
import android.provider.MediaStore
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
import com.panwar2001.pdfpro.ui.components.PdfViewer
import com.panwar2001.pdfpro.ui.components.sharePdfFile
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.appGraph(navActions: NavigationActions){

    navigation(route= com.panwar2001.pdfpro.screens.Screens.Home.route,
        startDestination= com.panwar2001.pdfpro.screens.Screens.Home.HomeScreen.route) {
        composable(route = com.panwar2001.pdfpro.screens.Screens.Home.HomeScreen.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<AppViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val pagerState = rememberPagerState { 2 }
            val context = LocalContext.current
            HomeScreen(
                onNavigationIconClick = navActions::openDrawer,
                pdfList = uiState.pdfsList,
                navigateTo = navActions::navigateTo,
                query = uiState.query,
                pagerState = pagerState,
                onQueryChange = { viewModel.setSearchText(it)
                    viewModel.searchPdfs()},
                onSearch = {
                    viewModel.setSearchBarActive(false)
                    viewModel.setSearchText(it)
                    viewModel.searchPdfs()
                },
                active = uiState.searchBarActive,
                onActiveChange = viewModel::setSearchBarActive,
                loading = false,
                showBottomSheet = uiState.isBottomSheetVisible,
                setBottomSheetState = viewModel::setBottomSheetVisible,
                shareFile = {
                    val baseUri = MediaStore.Files.getContentUri("external")
                    com.panwar2001.pdfpro.ui.components.sharePdfFile(
                        context,
                        ContentUris.withAppendedId(baseUri, it),
                        "application/pdf"
                    )
                },
                onPdfCardClick = {
                    viewModel.setUri(it)
                    navActions.navigateToScreen(com.panwar2001.pdfpro.screens.Screens.Home.PdfViewer.route)
                },
                options = viewModel.options,
                scrollToPage = {
                    if (it != pagerState.currentPage) navActions.scope.launch {
                        viewModel.setSearchBarActive(false)
                        pagerState.animateScrollToPage(
                            it
                        )
                    }
                },
                setSortBy = {
                    viewModel.setSortOption(it)
                    viewModel.searchPdfs()
                },
                toggleSortOrder = {
                    viewModel.toggleSortOrder()
                    viewModel.searchPdfs()
                },
                sortBy = uiState.sortOption,
                onSearchTrailingIconClick = {
                    if (uiState.query.isNotEmpty()) {
                        viewModel.setSearchText("")
                        viewModel.searchPdfs()
                    } else if (uiState.searchBarActive) {
                        viewModel.setSearchBarActive(false)
                    }
                }
            )
        }
        composable(route = com.panwar2001.pdfpro.screens.Screens.Home.LanguagePickerScreen.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<AppViewModel>(backStackEntry)
            val currentLocale = viewModel.getCurrentLocale()
            LanguagePickerScreen(navigateUp =navActions::navigateBack,
                viewModel.languages,
                currentLocale = currentLocale,
                setLocale = {
                    viewModel.setLocale(it)
                })
        }
        composable(route = com.panwar2001.pdfpro.screens.Screens.Home.PdfViewer.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<AppViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            com.panwar2001.pdfpro.ui.components.PdfViewer(
                uri = uiState.pdfUri,
                navigateUp = navActions::navigateBack,
                uiState.pdfName,
                uiState.numPages
            )
        }
    }
}