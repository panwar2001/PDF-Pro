package com.panwar2001.pdfpro.model

import android.graphics.Bitmap
import android.net.Uri

/**
 * Data class that represents the current UI state
 */
data class PdfToTextUiState(
    val uri: Uri,
    val isLoading:Boolean,
    val thumbnail: Bitmap,
    val pdfFileName: String,
    val textFileName: String,
    val text: String,
    val numPages:Int,
    val fileUniqueId: Long,
)
