package com.panwar2001.pdfpro.data

import android.net.Uri
import androidx.core.net.toFile
import com.panwar2001.pdfpro.view_models.Img2PdfUiState
import javax.inject.Inject
import javax.inject.Singleton

interface Img2PdfInterface{
   fun initImg2PdfUiState(): Img2PdfUiState
   suspend fun savePdfToExternalStorage(externalStoragePdfUri:Uri,internalStoragePdfUri: Uri)
}

@Singleton
class Img2PdfRepository @Inject constructor(): Img2PdfInterface{
    override fun initImg2PdfUiState(): Img2PdfUiState{
        return Img2PdfUiState(
            imageList = listOf(),
            isLoading = false,
            fileName  = "file",
            fileUri   = Uri.EMPTY,
            numPages  = 0
        )
    }

    override suspend fun savePdfToExternalStorage(externalStoragePdfUri: Uri, internalStoragePdfUri: Uri) {
        externalStoragePdfUri.toFile().writeBytes(internalStoragePdfUri.toFile().readBytes())
    }
}
