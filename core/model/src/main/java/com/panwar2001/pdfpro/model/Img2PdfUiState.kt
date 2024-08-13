package com.panwar2001.pdfpro.model

import android.net.Uri
/**
 * represents the current UI state
 */
data class Img2PdfUiState(
    val imageList: List<ImageInfo>,
    val isLoading:Boolean,
    val fileName: String,
    val fileUri: Uri,
    val numPages:Int
)
