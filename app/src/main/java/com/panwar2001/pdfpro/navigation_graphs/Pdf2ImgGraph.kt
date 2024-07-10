package com.panwar2001.pdfpro.navigation_graphs

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.Screens
import com.panwar2001.pdfpro.sharedViewModel
import com.panwar2001.pdfpro.ui.components.DeterminateIndicator
import com.panwar2001.pdfpro.ui.components.FilePickerScreen
import com.panwar2001.pdfpro.ui.components.PdfViewer
import com.panwar2001.pdfpro.ui.components.ProgressIndicator
import com.panwar2001.pdfpro.ui.pdfToImages.ImagesScreen
import com.panwar2001.pdfpro.ui.pdfToImages.PreviewFileScreen
import com.panwar2001.pdfpro.ui.view_models.PdfToImagesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.pdf2ImgGraph(navController: NavController,
                                 scope: CoroutineScope,
                                 drawerState: DrawerState){
    navigation(route= Screens.PdfToImage.route,
               startDestination= Screens.FilePicker.route){
        composable(route= Screens.FilePicker.route){backStackEntry->
            val viewModel = backStackEntry.sharedViewModel<PdfToImagesViewModel>(navController)
            val pdfPickerLauncher=rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = {uri->
                    if(uri!=null){
                        viewModel.setLoading(true)
                        viewModel.setUri(uri)
                        scope.launch {
                            viewModel.generateThumbnailFromPDF()
                        }
                        navController.navigate(Screens.PdfToImage.PreviewFile.route) {
                            if (drawerState.isOpen) {
                                scope.launch { drawerState.apply { close() } }
                            }
                        }
                    }
                })


            FilePickerScreen(onNavigationIconClick = {
                scope.launch { drawerState.apply { if (isClosed) open() else close() } }
            },
                onClick = {
                    pdfPickerLauncher.launch(arrayOf("application/pdf"))
                },
                tool= DataSource.getToolData(R.string.pdf2img))
        }
        composable(route= Screens.PdfToImage.PreviewFile.route) { backStackEntry->
            val viewModel = backStackEntry.sharedViewModel<PdfToImagesViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()

            if (uiState.isLoading) {
                ProgressIndicator(modifier = Modifier)
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
                    setLoading={loading:Boolean->viewModel.setLoading(loading)},
                    convertToImages={
                        scope.launch {
                            viewModel.generateImages()
                        }
                    },
                    tool= DataSource.getToolData(R.string.pdf2img)
                )
            }
        }
        composable(route= Screens.PdfToImage.PdfViewer.route){ backStackEntry->
            val viewModel = backStackEntry.sharedViewModel<PdfToImagesViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()
            PdfViewer(uri = uiState.uri,
                navigateUp ={navController.navigateUp()},
                uiState.fileName,
                uiState.numPages)
        }
        composable(route=Screens.PdfToImage.ImageScreen.route) { backStackEntry->
            val viewModel = backStackEntry.sharedViewModel<PdfToImagesViewModel>(navController)
            val uiState by viewModel.uiState.collectAsState()
            if (uiState.isLoading) {
                DeterminateIndicator(uiState.progress)
            } else {
                if (uiState.images.isEmpty()) {
                    DeterminateIndicator(uiState.progress)
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