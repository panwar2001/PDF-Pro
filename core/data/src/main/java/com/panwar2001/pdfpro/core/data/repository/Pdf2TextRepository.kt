package com.panwar2001.pdfpro.core.data.repository

import com.panwar2001.pdfpro.model.PdfToTextUiState
import com.panwar2001.pdfpro.model.TextFileInfo
import kotlinx.coroutines.flow.Flow

interface Pdf2TextRepository {
    fun initPdfToTextUiState(): PdfToTextUiState

    suspend fun createTextFile(text:String,fileName: String): Pair<Long,String>

    suspend fun deleteTextFile(id: Long)

    fun getAllTextFiles(): Flow<List<TextFileInfo>>

    suspend fun getTextAndNameFromFile(id: Long): Pair<String,String>

    suspend fun modifyName(id: Long, name: String)
}