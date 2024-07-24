package com.panwar2001.pdfpro.navigation

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import com.panwar2001.pdfpro.compose.pdfToImages.ImagesScreen
import com.panwar2001.pdfpro.compose.pdfToImages.PreviewFileScreen
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.view_models.PdfToImagesViewModel

fun NavGraphBuilder.pdf2ImgGraph(navActions: NavigationActions){
    navigation(route= Screens.PdfToImage.route,
               startDestination= Screens.FilePicker.route){
        composable(route= Screens.FilePicker.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToImagesViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            val pdfPickerLauncher=rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = {uri->
                    if(uri!=null){
                        viewModel.setLoading(true)
                        viewModel.setUri(uri)
                        viewModel.generateThumbnailFromPDF()
                        navActions.navigateToScreen(Screens.PdfToImage.PreviewFile.route)
                    }
                })


            FilePickerScreen(onNavigationIconClick = navActions::openDrawer,
                onClick = {
                    pdfPickerLauncher.launch(arrayOf("application/pdf"))
                },
                tool= DataSource.getToolData(R.string.pdf2img),
                isLoading = uiState.isLoading,
                userMessage = null,
                snackBarMessageShown = {},
                isError = false
            )
        }
        composable(route= Screens.PdfToImage.PreviewFile.route) { backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToImagesViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            Log.e("destination: ", backStackEntry.destination.parent?.route.toString())

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
        composable(route= Screens.PdfToImage.PdfViewer.route){ backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToImagesViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            PdfViewer(uri = uiState.uri,
                navigateUp = navActions::navigateBack,
                uiState.fileName,
                uiState.numPages)
        }
        composable(route= Screens.PdfToImage.ImageScreen.route) { backStackEntry->
            val viewModel = navActions.sharedViewModel<PdfToImagesViewModel>(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()
            val progress by viewModel.progress.collectAsState()

            if (uiState.isLoading || uiState.images.isEmpty()) {
                DeterminateIndicator(progress)
            } else {
                    ImagesScreen(images = uiState.images,
                        onNavigationIconClick = navActions::openDrawer
                    )
                }
            }
        }
    }