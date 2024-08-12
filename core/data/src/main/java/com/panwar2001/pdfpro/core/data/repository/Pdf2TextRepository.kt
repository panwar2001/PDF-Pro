package com.panwar2001.pdfpro.core.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.DocumentsContract
import androidx.core.content.FileProvider
import com.panwar2001.pdfpro.core.domain.GetFileSizeUseCase
import com.panwar2001.pdfpro.data.source.local.TextFile
import com.panwar2001.pdfpro.data.source.local.TextFileDao
import com.panwar2001.pdfpro.view_models.PdfToTextUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

interface Pdf2TextRepository {
    fun initPdfToTextUiState(): PdfToTextUiState

    suspend fun createTextFile(text:String,fileName: String): Pair<Long,String>

    suspend fun deleteTextFile(id: Long)

    fun getAllTextFiles(): Flow<List<TextFileInfo>>

    suspend fun getTextAndNameFromFile(id: Long): Pair<String,String>

    suspend fun modifyName(id: Long, name: String)
}




data class TextFileInfo(
    val fileName: String,
    val id: Long,
    val uri: Uri,
    val fileSize:String)