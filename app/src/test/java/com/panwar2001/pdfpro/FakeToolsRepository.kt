package com.panwar2001.pdfpro

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.graphics.createBitmap
import com.panwar2001.pdfpro.compose.PdfRow
import com.panwar2001.pdfpro.data.ImageInfo
import com.panwar2001.pdfpro.data.ToolsInterfaceRepository
import com.panwar2001.pdfpro.view_models.PdfToTextUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.mockito.Mockito.mock
import org.powermock.api.mockito.PowerMockito



class FakeToolsRepository() : ToolsInterfaceRepository {
    private val _progress = MutableStateFlow(0f)
    override val progress: StateFlow<Float> get() = _progress

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
            throw Exception("The uri is Empty")
//        else{
//            throw Exception("done")
//        }
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
        val bitmap: Bitmap = mock(Bitmap::class.java)
        return bitmap
    }

    override fun getUriFromMediaId(id: Long): Uri {
        TODO("Not yet implemented")
    }

    override fun initPdfToTextUiState(): PdfToTextUiState {
        return PdfToTextUiState(
            uri=mock(Uri::class.java),
            isLoading=false,
            thumbnail=getDefaultThumbnail(),
            fileName="file.pdf",
            text= "",
            numPages=0,
            userMessage=0,
            state="")
    }
}