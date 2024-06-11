package com.panwar2001.pdfpro

import android.net.Uri
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.ui.components.DeterminateIndicator
import com.panwar2001.pdfpro.ui.components.FilePickerScreen
import com.panwar2001.pdfpro.ui.components.PdfViewer
import com.panwar2001.pdfpro.ui.components.ProgressIndicator
import com.panwar2001.pdfpro.ui.pdfToImages.ImagesScreen
import com.panwar2001.pdfpro.ui.pdfToImages.PreviewFileScreen
import com.panwar2001.pdfpro.ui.view_models.PdfToImagesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.imgGraph(navController: NavController,
                             scope: CoroutineScope,
                             drawerState: DrawerState){

    navigation(route= Screens.PdfToImage.route,startDestination= Screens.FilePicker.route){
        composable(route= Screens.FilePicker.route){
            val viewModel = it.sharedViewModel<PdfToImagesViewModel>(navController)
            FilePickerScreen(onNavigationIconClick = {
                scope.launch { drawerState.apply { if (isClosed) open() else close() } }
            },
                setUri = {uri: Uri -> viewModel.setUri(uri)},
                setLoading={loading:Boolean->viewModel.setLoading(loading)},
                navigate = {
                    navController.navigate(Screens.PdfToImage.PreviewFile.route) {
                        if (drawerState.isOpen) {
                            scope.launch { drawerState.apply { close() } }
                        }
                    }
                },selectMultipleFile = false,
                mimeType = "application/pdf",
                tool= DataSource.getToolData(1))
        }
        composable(route= Screens.PdfToImage.PreviewFile.route) { model->
            val viewModel = model.sharedViewModel<PdfToImagesViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()
            if (uiState.isLoading) {
                ProgressIndicator(modifier = Modifier)
                viewModel.generateThumbnailFromPDF(LocalContext.current)
            } else {
                PreviewFileScreen(
                    onNavigationIconClick = {
                        scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                    },
                    navigateTo = { dest:String->
                        navController.navigate(dest) {
                            if (drawerState.isOpen) {
                                scope.launch { drawerState.apply { close() } }
                            }
                        }
                    },
                    thumbnail = uiState.thumbnail,
                    fileName = uiState.fileName,
                    setLoading={loading:Boolean->viewModel.setLoading(loading)}
                )
            }
        }
        composable(route= Screens.PdfToImage.PdfDisplay.route){ model->
            val viewModel = model.sharedViewModel<PdfToImagesViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()
            PdfViewer(uri = uiState.uri,
                navigateUp ={navController.navigateUp()},
                uiState.fileName)
        }
        composable(route=Screens.PdfToImage.ImageScreen.route) { model ->
            val viewModel = model.sharedViewModel<PdfToImagesViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()
            val progress by viewModel.progress.collectAsState()
            if (uiState.isLoading) {
                DeterminateIndicator(progress)
                viewModel.generateImages(LocalContext.current)
            } else {
                if (uiState.images.isEmpty()) {
                    DeterminateIndicator(progress)
                } else {
                    ImagesScreen(images = uiState.images,
                        onNavigationIconClick = {
                            scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                        }
                    )
                }
            }
        }
    }
}