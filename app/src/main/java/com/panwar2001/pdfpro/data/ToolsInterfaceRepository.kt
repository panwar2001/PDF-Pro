package com.panwar2001.pdfpro.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.panwar2001.pdfpro.compose.PdfRow
import com.panwar2001.pdfpro.view_models.PdfToTextUiState
import kotlinx.coroutines.flow.StateFlow

interface ToolsInterfaceRepository {
 val progress: StateFlow<Float>

 suspend fun saveFileToDownload(uri: Uri, context: Context)

 suspend fun searchPdfs(sortingOrder: Int, ascendingSort: Boolean, mimeType: String,query: String): List<PdfRow>

 suspend fun getThumbnailOfPdf(uri:Uri): Bitmap

 suspend fun getNumPages(uri:Uri):Int

 suspend fun getPdfName(uri: Uri): String

 suspend fun convertToText(uri: Uri): String

 fun getAppLocale(): String

 suspend fun setAppLocale(localeTag: String)

 suspend fun images2Pdf(imageList: List<ImageInfo>): Uri

 fun getImageInfo(uri:Uri):ImageInfo

 suspend fun pdfToImages(uri: Uri): List<Bitmap>

 fun getDefaultThumbnail(): Bitmap

 fun getUriFromMediaId(id: Long): Uri

 fun initPdfToTextUiState():PdfToTextUiState
}