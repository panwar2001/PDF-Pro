package com.panwar2001.pdfpro

import android.net.Uri
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.ui.components.FilePickerScreen
import com.panwar2001.pdfpro.ui.components.PdfViewer
import com.panwar2001.pdfpro.ui.images2pdf.ImagesDisplay
import com.panwar2001.pdfpro.ui.view_models.Img2pdfViewModel
import com.panwar2001.pdfpro.ui.view_models.PdfToImagesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.img2PdfGraph(navController: NavController,
                                 scope: CoroutineScope,
                                 drawerState: DrawerState){

    navigation(route= Screens.Img2Pdf.route,startDestination= Screens.FilePicker.route){
        composable(route= Screens.FilePicker.route){
            val viewModel = it.sharedViewModel<Img2pdfViewModel>(navController)
            FilePickerScreen(onNavigationIconClick = {
                scope.launch { drawerState.apply { if (isClosed) open() else close() } }
            },
                setUris = {uris: List<Uri> -> viewModel.setUris(uris)},
                setLoading={loading:Boolean->viewModel.setLoading(loading)},
                navigate = {
                    navController.navigate(Screens.Img2Pdf.ReorderScreen.route) {
                        if (drawerState.isOpen) {
                            scope.launch { drawerState.apply { close() } }
                        }
                    }
                },selectMultipleFile = true,
                mimeType = "image/*",
                tool= DataSource.getToolData(2))
        }
        composable(route= Screens.Img2Pdf.ReorderScreen.route) { model->
            val viewModel = model.sharedViewModel<Img2pdfViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()
            ImagesDisplay(
                    imgUris = uiState.Uris,
                    navigateUp ={navController.navigateUp()}
                )
        }
        composable(route= Screens.Img2Pdf.PdfViewer.route){ model->
            val viewModel = model.sharedViewModel<PdfToImagesViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()
//            PdfViewer(uri = uiState.uri,
//                navigateUp ={navController.navigateUp()},
//                uiState.fileName)
        }
//        composable(route=Screens.PdfToImage.ImageScreen.route) { model ->
//            val viewModel = model.sharedViewModel<PdfToImagesViewModel>(navController)
//            val uiState by viewModel.uiState.collectAsState()
//            if (uiState.isLoading) {
//                DeterminateIndicator(uiState.progress)
//            } else {
//                if (uiState.images.isEmpty()) {
//                    DeterminateIndicator(uiState.progress)
//                } else {
//                    ImagesScreen(images = uiState.images,
//                        onNavigationIconClick = {
//                            scope.launch { drawerState.apply { if (isClosed) open() else close() } }
//                        }
//                    )
//                }
//            }
//        }
    }
}