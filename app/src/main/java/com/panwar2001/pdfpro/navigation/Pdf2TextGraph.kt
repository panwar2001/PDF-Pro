package com.panwar2001.pdfpro.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
            val pdfPickerLauncher= rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = {uri->
                    if(uri!=null){
                        viewModel.setLoading(true)
                        viewModel.setUri(uri)
                        scope.launch {
                            viewModel.generateThumbnailFromPDF()
                        }
                        navActions.navigateToScreen(Screens.PdfToText.PreviewFile.route)
                    }
                })
            FilePickerScreen(onNavigationIconClick = navActions::toggleDrawer,
                onClick = {
                pdfPickerLauncher.launch(arrayOf("application/pdf"))
            },
                tool= DataSource.getToolData(R.string.pdf2text))
        }
        composable(route= Screens.PdfToText.PreviewFile.route) { backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToTextViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            if (uiState.isLoading) {
                ProgressIndicator(modifier = Modifier)
            } else {
                PreviewFileScreen(
                    onNavigationIconClick = navActions::toggleDrawer,
                    navigateTo = navActions::navigateToScreen,
                    thumbnail = uiState.thumbnail,
                    fileName = uiState.fileName,
                    setLoading={loading:Boolean->viewModel.setLoading(loading)},
                    convertToText={
                        scope.launch {
                            viewModel.convertToText()
                        }
                    },
                    tool = DataSource.getToolData(R.string.pdf2text)
                )
            }
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
            if(uiState.isLoading && uiState.text==""){
                ProgressIndicator(modifier = Modifier)
            }else {
                if(uiState.text!="") {
                    TextScreen(text = uiState.text,
                        onNavigationIconClick = navActions::toggleDrawer)
                }else{
                    ProgressIndicator(modifier = Modifier)
                }
            }
        }
    }
}