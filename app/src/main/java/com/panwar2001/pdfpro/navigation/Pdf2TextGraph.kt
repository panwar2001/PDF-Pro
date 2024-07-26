package com.panwar2001.pdfpro.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.MenuItem
import com.panwar2001.pdfpro.compose.components.FilePickerScreen
import com.panwar2001.pdfpro.compose.components.PdfViewer
import com.panwar2001.pdfpro.compose.components.sharePdfFile
import com.panwar2001.pdfpro.compose.pdfToText.PreviewFileScreen
import com.panwar2001.pdfpro.compose.pdfToText.TextFilesScreen
import com.panwar2001.pdfpro.compose.pdfToText.TextScreen
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.view_models.PdfToTextViewModel

fun NavGraphBuilder.pdf2txtGraph(navActions: NavigationActions){
    navigation(route= Screens.PdfToText.route,startDestination= Screens.FilePicker.route) {
        composable(route = Screens.FilePicker.route,) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val menuItems = remember {
                mutableListOf(MenuItem("Text Files Log") {
                    navActions.navigateToScreen(Screens.PdfToText.TextFilesScreen.route)
                })
            }
            val pdfPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = { uri ->
                    if (uri != null) {
                        viewModel.pickPdf(uri)
                    }
                })
            if(uiState.triggerSuccess){
                LaunchedEffect(Unit) {
                    viewModel.setTriggerSuccess(false)
                    navActions.navigateToScreen(Screens.PdfToText.PreviewFile.route)
                }
            }
            FilePickerScreen(
                onNavigationIconClick = navActions::openDrawer,
                onClick = {pdfPickerLauncher.launch(arrayOf("application/pdf"))},
                tool = DataSource.getToolData(R.string.pdf2text),
                menuItems= menuItems,
                isLoading = uiState.isLoading,
                userMessage= uiState.userMessage,
                snackBarMessageShown = viewModel::snackBarMessageShown,
                isError = uiState.isError
            )
        }
        composable(route = Screens.PdfToText.PreviewFile.route) { backStackEntry ->
            val menuItems = remember {
                mutableListOf(MenuItem("Text Files Log") {
                    navActions.navigateToScreen(Screens.PdfToText.TextFilesScreen.route)
                })
            }

            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()


            LaunchedEffect(uiState.triggerSuccess) {
                if(uiState.triggerSuccess){
                    viewModel.setTriggerSuccess(false)
                    navActions.navigateToScreen(Screens.PdfToText.TextScreen.route)
                }
            }

            PreviewFileScreen(
                onNavigationIconClick = navActions::openDrawer,
                thumbnail = uiState.thumbnail,
                fileName = uiState.pdfFileName,
                navigateToPdfViewer = { navActions.navigateToScreen(Screens.PdfToText.PdfViewer.route)},
                convertToText = {viewModel.convertToText()},
                tool = DataSource.getToolData(R.string.pdf2text),
                menuItems = menuItems,
                isLoading = uiState.isLoading,
                snackBarMessageShown = viewModel::snackBarMessageShown,
                isError = uiState.isError,
                userMessage = uiState.userMessage
            )

        }

        composable(route = Screens.PdfToText.TextFilesScreen.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val filesInfo = viewModel.allFilesInfo.collectAsState(initial = listOf()).value
            val context = LocalContext.current

            LaunchedEffect(uiState.triggerSuccess) {
                if(uiState.triggerSuccess){
                    viewModel.setTriggerSuccess(false)
                    navActions.navigateToScreen(Screens.PdfToText.TextScreen.route)
                }
            }

            TextFilesScreen(
                filesInfo = filesInfo,
                navigateBack = navActions::navigateBack,
                deleteFile = viewModel::deleteFile,
                share = {
                    sharePdfFile(context, it, "text/plain")
                },
                viewTextFile = {viewModel.readTextFromFile(it)},
                isLoading = uiState.isLoading,
                snackBarMessageShown = viewModel::snackBarMessageShown,
                isError = uiState.isError,
                userMessage = uiState.userMessage
            )
        }
        composable(route = Screens.PdfToText.PdfViewer.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            PdfViewer(
                uri = uiState.uri,
                navigateUp = navActions::navigateBack,
                uiState.pdfFileName,
                uiState.numPages
            )
        }
        composable(route = Screens.PdfToText.TextScreen.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            TextScreen(
                text = uiState.text,
                onNavigationIconClick = navActions::openDrawer,
                onBackPress = {
                    viewModel.setPdfText("")
                    navActions.navigateBack()
                },
                fileName = uiState.textFileName,
                modifyFileName = viewModel::modifyFileName,
            )
        }
    }
}
