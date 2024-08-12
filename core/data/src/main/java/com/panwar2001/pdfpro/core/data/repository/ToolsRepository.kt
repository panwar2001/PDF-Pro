package com.panwar2001.pdfpro.core.data.repository

import android.app.LocaleManager
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toFile
import androidx.core.os.LocaleListCompat
import androidx.documentfile.provider.DocumentFile
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.PdfRow
import com.panwar2001.pdfpro.core.domain.GetFileSizeUseCase
import com.panwar2001.pdfpro.model.ImageInfo
import com.panwar2001.pdfpro.view_models.AppUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


interface ToolsRepository {
    val progress: StateFlow<Float>

    suspend fun searchPdfs(sortingOrder: Int, ascendingSort: Boolean, mimeType: String,query: String): List<PdfRow>

    fun getImageInfo(uri:Uri,isDocScanUri:Boolean=false): ImageInfo

    fun getDefaultThumbnail(): Bitmap

    fun getUriFromMediaId(id: Long): Uri

    fun initAppUiState(): AppUiState
}

