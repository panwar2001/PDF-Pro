package com.panwar2001.pdfpro.core.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.panwar2001.pdfpro.model.AppUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * https://developer.android.com/codelabs/basic-android-kotlin-compose-add-repository#3
 */

class ToolsRepositoryImpl @Inject
constructor(
    @ApplicationContext private val context: Context
): ToolsRepository {
    private val _progress = MutableStateFlow(0f)
    override val progress: StateFlow<Float> get() = _progress

    override suspend fun searchPdfs(sortingOrder: Int,
                                    ascendingSort: Boolean,
                                    mimeType: String,
                                    query:String){

    }
    private fun Long.toTimeDateString(): String {
        val dateTime = Date(this*1000)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
        return format.format(dateTime)
    }


    override fun getDefaultThumbnail(): Bitmap {
        return Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888)
    }

    override fun initAppUiState(): AppUiState {
        return AppUiState(
            text="",
            query="",
            isAscending=false,
            sortOption=3,
            searchBarActive= false,
            isBottomSheetVisible=false,
            pdfUri= Uri.EMPTY,
            pdfName="",
            numPages=0)
    }

    override fun getFileSize(len:Long?): String=when {
            len==null-> "0 Byte"
            len < 1024 -> "$len Byte"
            len < 1048576 -> "%.1f KB".format(len / 1024.0)
            len < 1073741824 -> "%.1f MB".format(len / 1048576.0)
            else -> "%.1f GB".format(len / 1073741824.0)
        }
}