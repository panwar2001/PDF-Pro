package com.panwar2001.pdfpro.core.data.repository

import android.net.Uri
import com.panwar2001.pdfpro.model.ImageInfo
import com.panwar2001.pdfpro.model.Img2PdfUiState
import kotlinx.coroutines.flow.StateFlow

interface Img2PdfRepository{

    val progress: StateFlow<Float>

    fun initImg2PdfUiState(): Img2PdfUiState

    suspend fun savePdfToExternalStorage(externalStoragePdfUri:Uri,internalStoragePdfUri: Uri)

    suspend fun images2Pdf(imageList: List<ImageInfo>, maxHeight: Int=842, maxWidth: Int= 595): Uri

    suspend fun renamePdfFile(uri:Uri,newFileName: String):Uri

    fun getImageInfo(uri: Uri, isDocScanUri:Boolean):ImageInfo
}
