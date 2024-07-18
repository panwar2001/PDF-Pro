package com.panwar2001.pdfpro.navigation

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.components.FilePickerScreen
import com.panwar2001.pdfpro.compose.components.PdfViewer
import com.panwar2001.pdfpro.compose.components.ProgressIndicator
import com.panwar2001.pdfpro.compose.pdfToText.PreviewFileScreen
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
            uiState.userMessage?.let {
                val message= stringResource(id = it)
                LaunchedEffect(uiState.userMessage) {
                    snackBarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
                    viewModel.snackBarMessageShown()
                    viewModel.setCompleted(false)
                }
            }
            LaunchedEffect(uiState.isCompleted,uiState.userMessage) {
                    if(uiState.isCompleted && uiState.userMessage==null) {
                        viewModel.setCompleted(false)
                        navActions.navigateToScreen(Screens.PdfToText.PreviewFile.route)
                    }
                }
            val pdfPickerLauncher= rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = {uri->
                    if(uri!=null){
                        viewModel.setLoading(true)
                        viewModel.setUri(uri)
                        viewModel.generateThumbnailFromPDF()
                    }
                },)

            if (uiState.isLoading) {
                ProgressIndicator(modifier = Modifier)
            } else {
                FilePickerScreen(
                    onNavigationIconClick = navActions::toggleDrawer,
                    onClick = {
                        pdfPickerLauncher.launch(arrayOf("application/pdf"))
                    },
                    tool = DataSource.getToolData(R.string.pdf2text),
                    snackBarHostState
                )
            }
        }
        composable(route= Screens.PdfToText.PreviewFile.route) { backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            // Check for user messages to display on the screen
                PreviewFileScreen(
                    onNavigationIconClick = navActions::toggleDrawer,
                    navigateTo = navActions::navigateToScreen,
                    thumbnail = uiState.thumbnail,
                    fileName = uiState.fileName,
                    setLoading={loading:Boolean->viewModel.setLoading(loading)},
                    convertToText={
                            viewModel.convertToText()
                    },
                    tool = DataSource.getToolData(R.string.pdf2text)
                )
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
            if(uiState.isLoading ){
                ProgressIndicator(modifier = Modifier)
            }else {
                    TextScreen(text = uiState.text,
                        onNavigationIconClick = navActions::toggleDrawer,
                        onBackPress = {
                            viewModel.setPdfText("")
                            navActions.navigateBack()
                        }
                    )
            }
        }
    }
}