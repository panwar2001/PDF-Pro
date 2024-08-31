package com.panwar2001.pdfpro.core.data.pdf2TextTest

import android.graphics.Bitmap
import android.net.Uri
import com.panwar2001.pdfpro.compose.PdfRow
import com.panwar2001.pdfpro.data.ImageInfo
import com.panwar2001.pdfpro.data.ToolsInterface
import com.panwar2001.pdfpro.view_models.AppUiState
import io.mockk.mockk
import kotlinx.coroutines.flow.StateFlow

class ToolsFakeRepository(override val progress: StateFlow<Float>) : ToolsInterface {

    override suspend fun searchPdfs(
        sortingOrder: Int,
        ascendingSort: Boolean,
        mimeType: String,
        query: String
    ): List<PdfRow> {
        return listOf()
    }

    override suspend fun getThumbnailOfPdf(uri: Uri): Bitmap {
        return mockk()
    }

    override suspend fun getNumPages(uri: Uri): Int {
        return 1
    }

    override suspend fun getPdfName(uri: Uri): String {
        return ""
    }

    override fun getAppLocale(): String {
        return "en"
    }

    override suspend fun setAppLocale(localeTag: String) {
    }

    override fun getImageInfo(uri: Uri, isDocScanUri: Boolean): ImageInfo {
        return mockk()
    }

    override fun getDefaultThumbnail(): Bitmap {
        return mockk()
    }

    override fun getUriFromMediaId(id: Long): Uri {
        return mockk()
    }

    override fun initAppUiState(): AppUiState {
        return mockk()
    }
}
