package com.panwar2001.pdfpro.feature.pdf2img

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.ui.components.DeterminateIndicator
import com.panwar2001.pdfpro.ui.components.FilePickerScreen
import com.panwar2001.pdfpro.ui.components.PdfViewer
import com.panwar2001.pdfpro.compose.pdfToImages.ImagesScreen
import com.panwar2001.pdfpro.compose.pdfToImages.PreviewFileScreen
import com.panwar2001.pdfpro.data.DataSource

fun NavGraphBuilder.pdf2ImgGraph(navActions: NavigationActions){
    navigation(route= com.panwar2001.pdfpro.screens.Screens.PdfToImage.route,
               startDestination= com.panwar2001.pdfpro.screens.Screens.FilePicker.route){
        composable(route= com.panwar2001.pdfpro.screens.Screens.FilePicker.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToImagesViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val snackBarHostState = remember { SnackbarHostState() }

            val pdfPickerLauncher=rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = {uri->
                    if(uri!=null){
                        viewModel.setLoading(true)
                        viewModel.setUri(uri)
                        viewModel.generateThumbnailFromPDF()
                        navActions.navigateToScreen(com.panwar2001.pdfpro.screens.Screens.PdfToImage.PreviewFile.route)
                    }
                })


            com.panwar2001.pdfpro.ui.components.FilePickerScreen(
                onNavigationIconClick = navActions::openDrawer,
                onClick = {
                    pdfPickerLauncher.launch(arrayOf("application/pdf"))
                },
                tool = DataSource.getToolData(R.string.pdf2img),
                isLoading = uiState.isLoading,
                snackBarHostState = snackBarHostState
            )
        }
        composable(route= com.panwar2001.pdfpro.screens.Screens.PdfToImage.PreviewFile.route) { backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToImagesViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

                PreviewFileScreen(
                    onNavigationIconClick = navActions::openDrawer,
                    navigateTo = navActions::navigateToScreen,
                    thumbnail = uiState.thumbnail,
                    fileName = uiState.fileName,
                    setLoading={loading:Boolean->viewModel.setLoading(loading)},
                    convertToImages={
                            viewModel.generateImages()
                    },
                    tool= DataSource.getToolData(R.string.pdf2img),
                )
        }
        composable(route= com.panwar2001.pdfpro.screens.Screens.PdfToImage.PdfViewer.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToImagesViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            com.panwar2001.pdfpro.ui.components.PdfViewer(
                uri = uiState.uri,
                navigateUp = navActions::navigateBack,
                uiState.fileName,
                uiState.numPages
            )
        }
        composable(route= com.panwar2001.pdfpro.screens.Screens.PdfToImage.ImageScreen.route) { backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToImagesViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val progress by viewModel.progress.collectAsState()

            if (uiState.isLoading || uiState.images.isEmpty()) {
                com.panwar2001.pdfpro.ui.components.DeterminateIndicator(progress)
            } else {
                    ImagesScreen(images = uiState.images,
                        onNavigationIconClick = navActions::openDrawer)
                }
            }
        }
    }