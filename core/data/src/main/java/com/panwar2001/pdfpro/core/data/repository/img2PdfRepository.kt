package com.panwar2001.pdfpro.core.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import com.panwar2001.pdfpro.model.ImageInfo
import com.panwar2001.pdfpro.view_models.Img2PdfUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

interface Img2PdfRepository{

    val progress: StateFlow<Float>

    fun initImg2PdfUiState(): Img2PdfUiState

    suspend fun savePdfToExternalStorage(externalStoragePdfUri:Uri,internalStoragePdfUri: Uri)

    suspend fun images2Pdf(imageList: List<ImageInfo>, maxHeight: Int=842, maxWidth: Int= 595): Uri

    suspend fun renamePdfFile(uri:Uri,newFileName: String):Uri
}
