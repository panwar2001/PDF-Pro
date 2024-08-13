package com.panwar2001.pdfpro.model

import android.graphics.Bitmap
import android.net.Uri

/**
 * Data class that represents the current UI state in terms of  and [uri]
 */
data class PdfToImagesUiState(
    val uri: Uri,
    val isLoading:Boolean,
    val thumbnail: Bitmap,
    val fileName: String,
    val images:List<Bitmap>,
    val numPages:Int
)
