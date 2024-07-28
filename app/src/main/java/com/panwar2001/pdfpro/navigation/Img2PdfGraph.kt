package com.panwar2001.pdfpro.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.components.DeterminateIndicator
import com.panwar2001.pdfpro.compose.components.FilePickerScreen
import com.panwar2001.pdfpro.compose.components.PdfViewer
import com.panwar2001.pdfpro.compose.images2pdf.ImagesDisplay
import com.panwar2001.pdfpro.compose.images2pdf.ReorderScreen
import com.panwar2001.pdfpro.compose.images2pdf.SavePdfScreen
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.view_models.Img2pdfViewModel

fun NavGraphBuilder.img2PdfGraph(navActions: NavigationActions){

    navigation(route= Screens.Img2Pdf.route,
               startDestination= Screens.FilePicker.route){

        composable(route= Screens.FilePicker.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<Img2pdfViewModel>(backStackEntry)
            val imagesPickerLauncher= rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickMultipleVisualMedia() ,
                onResult = {uris->
                    if(uris.isNotEmpty()) {
                        viewModel.setUris(uris)
                        viewModel.setLoading(true)
                        navActions.navigateToScreen(Screens.Img2Pdf.ImagesViewScreen.route)
                    } })
            FilePickerScreen(onNavigationIconClick = navActions::openDrawer,
                onClick = {
                    imagesPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                tool= DataSource.getToolData(R.string.img2pdf),
                userMessage = null,
                snackBarMessageShown = {},
                isError = false)
        }
        composable(route= Screens.Img2Pdf.ReorderScreen.route) { backStackEntry ->
            val viewModel = navActions.sharedViewModel<Img2pdfViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            ReorderScreen(navigateUp = navActions::navigateBack,
                          imageList =  uiState.imageList.map{it.uri},
                          onMove = viewModel::move)
        }
        composable(route= Screens.Img2Pdf.ImagesViewScreen.route) { backStackEntry->
            val viewModel = navActions.sharedViewModel<Img2pdfViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            ImagesDisplay(
                imageList = uiState.imageList,
                navigateUp = navActions::navigateBack,
                addImgUris = viewModel::addImgUris,
                addDocScanUris= viewModel::addDocScanUris,
                navigateToReorder = {
                    navActions.navigateToScreen(Screens.Img2Pdf.ReorderScreen.route)
                },
                toggleCheckBox = viewModel::setCheckBoxState,
                deleteImages =viewModel::deleteImages,
                convertToPdf = {
                    viewModel.setLoading(true)
                    navActions.navigateToScreen(Screens.Img2Pdf.SavePdfScreen.route)
                    viewModel.convert2Pdf()
                }
            )
        }

        composable(route= Screens.Img2Pdf.PdfViewer.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<Img2pdfViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            PdfViewer(uri = uiState.fileUri,
                navigateUp = navActions::navigateBack,
                fileName = uiState.fileName,
                numPages = uiState.numPages)
        }
        composable(route= Screens.Img2Pdf.SavePdfScreen.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<Img2pdfViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val progress by viewModel.progress.collectAsState()
            if (uiState.isLoading) {
                DeterminateIndicator(progress)
            } else {
                    SavePdfScreen(
                        backNavigate = navActions::navigateBack,
                        navigateToPdfViewer = {navActions.navigateToScreen(Screens.Img2Pdf.PdfViewer.route)},
                        fileName = uiState.fileName,
                        uri = uiState.imageList.first().uri,
                        fileUri=uiState.fileUri,
                        savePdfToExternalStorage = viewModel::savePdfToExternalStorage,
                        renamePdfFile=viewModel::renamePdfFile
            )
        }
    }
    }
}