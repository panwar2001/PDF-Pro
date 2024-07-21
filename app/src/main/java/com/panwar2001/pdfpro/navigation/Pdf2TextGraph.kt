package com.panwar2001.pdfpro.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.MenuItem
import com.panwar2001.pdfpro.compose.components.FilePickerScreen
import com.panwar2001.pdfpro.compose.components.PdfViewer
import com.panwar2001.pdfpro.compose.components.ProgressIndicator
import com.panwar2001.pdfpro.compose.components.sharePdfFile
import com.panwar2001.pdfpro.compose.pdfToText.PreviewFileScreen
import com.panwar2001.pdfpro.compose.pdfToText.TextFilesScreen
import com.panwar2001.pdfpro.compose.pdfToText.TextScreen
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.view_models.PdfToTextViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.pdf2txtGraph(scope:CoroutineScope,
                                 navActions: NavigationActions){
    navigation(route= Screens.PdfToText.route,startDestination= Screens.FilePicker.route){
        composable(route= Screens.FilePicker.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val snackBarHostState = remember { SnackbarHostState() }
            val menuItems= remember{ mutableListOf(MenuItem("Text Files Log") {
                navActions.navigateToScreen(Screens.PdfToText.TextFilesScreen.route)
            }) }
            val pdfPickerLauncher= rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = {uri->
                    if(uri!=null){
                        viewModel.setLoading(true)
                        viewModel.setUri(uri)
                        viewModel.generateThumbnailFromPDF()
                    }
                })

            if(uiState.isLoading) {
                    ProgressIndicator(modifier = Modifier)
            }
            else{
            when(uiState.state){
                "error"-> {
                    val message = stringResource(id = uiState.userMessage)
                    LaunchedEffect(Unit) {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                    viewModel.setState("")
                    viewModel.setSnackBarMessage(0)
                }
                "success"->{
                    viewModel.setState("")
                    navActions.navigateToScreen(Screens.PdfToText.PreviewFile.route)
                }
                else ->{
                    FilePickerScreen(
                        onNavigationIconClick = navActions::toggleDrawer,
                        onClick = {
                            pdfPickerLauncher.launch(arrayOf("application/pdf"))
                        },
                        tool = DataSource.getToolData(R.string.pdf2text),
                        snackBarHostState,
                        menuItems
                    )
                }
                }
            }
        }
        composable(route= Screens.PdfToText.PreviewFile.route) { backStackEntry->
            val menuItems= remember{ mutableListOf(MenuItem("Text Files Log") {
                navActions.navigateToScreen(Screens.PdfToText.TextFilesScreen.route)
            }) }

            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val snackBarHostState = remember { SnackbarHostState() }

            // Check for user messages to display on the screen
            if(uiState.isLoading) {
                ProgressIndicator(modifier = Modifier)
            }
            else {
                when (uiState.state) {
                    "error"->{
                        val message = stringResource(id = uiState.userMessage)
                        LaunchedEffect(Unit) {
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                        viewModel.setState("")
                        viewModel.setSnackBarMessage(0)
                    }
                    "success"->{
                        viewModel.setState("")
                        navActions.navigateToScreen(Screens.PdfToText.TextScreen.route)
                    }
                    else->PreviewFileScreen(
                        onNavigationIconClick = navActions::toggleDrawer,
                        thumbnail = uiState.thumbnail,
                        fileName = uiState.fileName,
                        navigateToPdfViewer = { navActions.navigateToScreen(Screens.PdfToText.PdfViewer.route)},
                        setLoading = { loading: Boolean -> viewModel.setLoading(loading) },
                        convertToText = {
                            viewModel.convertToText()
                        },
                        tool = DataSource.getToolData(R.string.pdf2text),
                        snackBarHostState = snackBarHostState,
                        menuItems=menuItems
                    )
                }
            }
        }
        composable(route= Screens.PdfToText.TextFilesScreen.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val filesInfo= viewModel.allFilesInfo.collectAsState(initial = listOf()).value
            val context= LocalContext.current
            TextFilesScreen(filesInfo = filesInfo,
                            navigateBack =  navActions::navigateBack,
                            deleteFile = viewModel::deleteFile,
                            share={
                                sharePdfFile(context,it,"text/plain")
                            },
                            viewTextFile = {
                                viewModel.readTextFromFile(it)
                                navActions.navigateToScreen(Screens.PdfToText.TextScreen.route)
                            })
        }
        composable(route= Screens.PdfToText.PdfViewer.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            PdfViewer(uri = uiState.uri,
                navigateUp = navActions::navigateBack,
                uiState.fileName,
                uiState.numPages)
        }
        composable(route= Screens.PdfToText.TextScreen.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
                    TextScreen(text = uiState.text,
                        onNavigationIconClick = navActions::toggleDrawer,
                        onBackPress = {
                            viewModel.setPdfText("")
                            navActions.navigateBack()
                        },
                        fileName=uiState.fileName,
                        modifyFileName=viewModel::modifyFileName,
                    )
            }
    }
}