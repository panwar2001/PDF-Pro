package com.panwar2001.pdfpro

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.panwar2001.pdfpro.compose.PdfRow
import com.panwar2001.pdfpro.data.ImageInfo
import com.panwar2001.pdfpro.data.ToolsInterfaceRepository
import kotlinx.coroutines.flow.StateFlow

class FakeToolsRepository(override val progress: StateFlow<Float>) : ToolsInterfaceRepository {
    override suspend fun saveFileToDownload(uri: Uri, context: Context) {
        TODO("Not yet implemented")
    }

    override suspend fun searchPdfs(
        sortingOrder: Int,
        ascendingSort: Boolean,
        mimeType: String,
        query: String
    ): List<PdfRow> {
        val pdfs= mutableListOf<PdfRow>()
        pdfs.add(PdfRow("18/03/2004","name.pdf",12f,0))
        return pdfs
    }

    override suspend fun getThumbnailOfPdf(uri: Uri): Bitmap {
        if(uri==Uri.EMPTY){
            throw Exception("The uri is Empty")
        }
        TODO("test")
    }

    override suspend fun getNumPages(uri: Uri): Int {
        return 1
    }

    override suspend fun getPdfName(uri: Uri): String {
        return "name.pdf"
    }

    override suspend fun convertToText(uri: Uri): String {
      return "Converted To Text"
    }

    override fun getAppLocale(): String {
        TODO("Not yet implemented")
    }

    override suspend fun setAppLocale(localeTag: String) {
        TODO("Not yet implemented")
    }

    override suspend fun images2Pdf(imageList: List<ImageInfo>): Uri {
        TODO("Not yet implemented")
    }

    override fun getImageInfo(uri: Uri): ImageInfo {
        TODO("Not yet implemented")
    }

    override suspend fun pdfToImages(uri: Uri): List<Bitmap> {
        TODO("Not yet implemented")
    }

    override fun getDefaultThumbnail(): Bitmap {
        TODO("Not yet implemented")
    }


}