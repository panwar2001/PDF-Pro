package com.panwar2001.pdfpro.data

import android.graphics.Bitmap
import android.net.Uri
import com.panwar2001.pdfpro.view_models.PdfToImagesUiState
import javax.inject.Inject
import javax.inject.Singleton

interface Pdf2ImgRepositoryInterface{
    fun initPdfToImagesUiState(): PdfToImagesUiState
}

@Singleton
class Pdf2ImgRepository @Inject constructor() :Pdf2ImgRepositoryInterface{
    override fun initPdfToImagesUiState(): PdfToImagesUiState {
        return PdfToImagesUiState(
            uri= Uri.EMPTY,
            isLoading=false,
            thumbnail= Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
            fileName="file.pdf",
            images= listOf(),
            numPages=0
        )
    }
}


