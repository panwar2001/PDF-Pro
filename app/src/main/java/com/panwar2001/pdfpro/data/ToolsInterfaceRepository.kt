package com.panwar2001.pdfpro.data

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.panwar2001.pdfpro.compose.PdfRow

interface ToolsInterfaceRepository {
 suspend  fun saveFileToDownload(uri: Uri, context: Context)

 suspend  fun searchPdfs(sortingOrder: Int, ascendingSort: Boolean, mimeType: String,query: String): List<PdfRow>

 suspend  fun getThumbnailOfPdf(uri:Uri): ImageBitmap?

 suspend  fun getNumPages(uri:Uri):Int

 suspend  fun getPdfName(uri: Uri): String

 suspend  fun convertToText(uri: Uri): String
}