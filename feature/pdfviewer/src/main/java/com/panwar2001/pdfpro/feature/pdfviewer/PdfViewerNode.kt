package com.panwar2001.pdfpro.feature.pdfviewer

fun tsr{
    composable(route = com.panwar2001.pdfpro.screens.Screens.Home.PdfViewer.route) { backStackEntry ->
        val viewModel = navActions.sharedViewModel<AppViewModel>(backStackEntry)
        val uiState by viewModel.uiState.collectAsState()

        com.panwar2001.pdfpro.ui.components.PdfViewer(
            uri = uiState.pdfUri,
            navigateUp = navActions::navigateBack,
            uiState.pdfName,
            uiState.numPages
        )
    }
}