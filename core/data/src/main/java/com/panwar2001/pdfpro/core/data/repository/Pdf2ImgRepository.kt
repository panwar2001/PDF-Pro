package com.panwar2001.pdfpro.core.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.panwar2001.pdfpro.model.PdfToImagesUiState
import kotlinx.coroutines.flow.StateFlow

interface Pdf2ImgRepository{
    val progress: StateFlow<Float>
    fun initPdfToImagesUiState(): PdfToImagesUiState
    suspend fun pdfToImages(uri: Uri): List<Bitmap>
}

