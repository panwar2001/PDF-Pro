package com.panwar2001.pdfpro.feature.pdf2txt

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
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
import com.panwar2001.pdfpro.ui.components.CustomSnackBarVisuals
import com.panwar2001.pdfpro.ui.components.FilePickerScreen
import com.panwar2001.pdfpro.ui.components.PdfViewer
import com.panwar2001.pdfpro.ui.components.passwordInputDialogState
import com.panwar2001.pdfpro.ui.components.sharePdfFile
import com.panwar2001.pdfpro.compose.pdfToText.PreviewFileScreen
import com.panwar2001.pdfpro.compose.pdfToText.TextFilesScreen
import com.panwar2001.pdfpro.compose.pdfToText.TextScreen
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.core.domain.EventType
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.pdf2txtGraph(navActions: NavigationActions){
    navigation(route= com.panwar2001.pdfpro.screens.Screens.PdfToText.route,startDestination= com.panwar2001.pdfpro.screens.Screens.FilePicker.route) {
        composable(route = com.panwar2001.pdfpro.screens.Screens.FilePicker.route,) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val context= LocalContext.current
            val snackBarHostState = remember { SnackbarHostState() }
            val passwordDialogState=
                com.panwar2001.pdfpro.ui.components.passwordInputDialogState(onConfirm = {
                    viewModel.unlockPdfAndUpload(uri = uiState.uri, password = it)
                })
            val menuItems = remember {
                mutableListOf(MenuItem(R.string.text_files_log) {
                    navActions.navigateToScreen(com.panwar2001.pdfpro.screens.Screens.PdfToText.TextFilesScreen.route)
                })
            }

            val pdfPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = { uri ->
                    if (uri != null) {
                        viewModel.uploadPdf(uri)
                    }
                })

            LaunchedEffect(viewModel.uiEventFlow) {
                viewModel.uiEventFlow.collectLatest {
                    when (it) {
                        EventType.Success -> {
                            passwordDialogState.hide()
                            navActions.navigateToScreen(com.panwar2001.pdfpro.screens.Screens.PdfToText.PreviewFile.route)
                        }
                        EventType.Error ->
                            snackBarHostState.showSnackbar(
                                com.panwar2001.pdfpro.ui.components.CustomSnackBarVisuals(
                                    message = context.getString(R.string.error_message),
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Short,
                                    isError = true
                                )
                            )
                        EventType.ShowDialog-> passwordDialogState.show()
                        EventType.ShowDialogError-> passwordDialogState.wrongPassword()
                    }
                }
            }
            com.panwar2001.pdfpro.ui.components.FilePickerScreen(
                onNavigationIconClick = navActions::openDrawer,
                onClick = { pdfPickerLauncher.launch(arrayOf("application/pdf")) },
                tool = DataSource.getToolData(R.string.pdf2text),
                menuItems = menuItems,
                isLoading = uiState.isLoading,
                snackBarHostState = snackBarHostState
            )
        }
        composable(route = com.panwar2001.pdfpro.screens.Screens.PdfToText.PreviewFile.route) { backStackEntry ->
            val menuItems = remember {
                mutableListOf(MenuItem(R.string.text_files_log) {
                    navActions.navigateToScreen(com.panwar2001.pdfpro.screens.Screens.PdfToText.TextFilesScreen.route)
                })
            }
            val context= LocalContext.current
            val snackBarHostState = remember { SnackbarHostState() }

            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(viewModel.uiEventFlow) {
                viewModel.uiEventFlow.collectLatest {
                    if(EventType.Success==it){
                        navActions.navigateToScreen(com.panwar2001.pdfpro.screens.Screens.PdfToText.TextScreen.route)
                    }else if(EventType.Error==it){
                        snackBarHostState.showSnackbar(
                            com.panwar2001.pdfpro.ui.components.CustomSnackBarVisuals(
                                message = context.getString(R.string.error_message),
                                withDismissAction = true,
                                duration = SnackbarDuration.Short,
                                isError = true
                            )
                        )
                    }
                }
            }


            PreviewFileScreen(
                onNavigationIconClick = navActions::openDrawer,
                thumbnail = uiState.thumbnail,
                fileName = uiState.pdfFileName,
                navigateToPdfViewer = { navActions.navigateToScreen(com.panwar2001.pdfpro.screens.Screens.PdfToText.PdfViewer.route)},
                convertToText = {viewModel.convertToText(uiState.uri,uiState.pdfFileName)},
                tool = DataSource.getToolData(R.string.pdf2text),
                menuItems = menuItems,
                isLoading = uiState.isLoading
            )

        }

        composable(route = com.panwar2001.pdfpro.screens.Screens.PdfToText.TextFilesScreen.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val filesInfo = viewModel.allFilesInfo.collectAsState(initial = listOf()).value
            val context = LocalContext.current
            val snackBarHostState = remember { SnackbarHostState() }

            LaunchedEffect(viewModel.uiEventFlow) {
                viewModel.uiEventFlow.collectLatest {
                    if(EventType.Success==it){
                            navActions.navigateToScreen(com.panwar2001.pdfpro.screens.Screens.PdfToText.TextScreen.route)
                        }
                    else if(EventType.Error==it){
                            snackBarHostState.showSnackbar(
                                com.panwar2001.pdfpro.ui.components.CustomSnackBarVisuals(
                                    message = context.getString(R.string.error_message),
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Short,
                                    isError = true
                                )
                            )
                        }
                    }
            }

            TextFilesScreen(
                filesInfo = filesInfo,
                navigateBack = navActions::navigateBack,
                deleteFile = viewModel::deleteFile,
                share = {
                    com.panwar2001.pdfpro.ui.components.sharePdfFile(context, it, "text/plain")
                },
                viewTextFile = {viewModel.readTextFromFile(it)},
                isLoading = uiState.isLoading
            )
        }
        composable(route = com.panwar2001.pdfpro.screens.Screens.PdfToText.PdfViewer.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            com.panwar2001.pdfpro.ui.components.PdfViewer(
                uri = uiState.uri,
                navigateUp = navActions::navigateBack,
                uiState.pdfFileName,
                uiState.numPages
            )
        }
        composable(route = com.panwar2001.pdfpro.screens.Screens.PdfToText.TextScreen.route) { backStackEntry ->
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
