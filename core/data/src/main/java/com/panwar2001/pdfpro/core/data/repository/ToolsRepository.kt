package com.panwar2001.pdfpro.core.data.repository

import android.graphics.Bitmap
import com.panwar2001.pdfpro.model.AppUiState
import kotlinx.coroutines.flow.StateFlow


interface ToolsRepository {
    val progress: StateFlow<Float>

    suspend fun searchPdfs(sortingOrder: Int, ascendingSort: Boolean, mimeType: String,query: String)

    fun getDefaultThumbnail(): Bitmap

    fun initAppUiState(): AppUiState

    fun getFileSize(len:Long?): String
}

