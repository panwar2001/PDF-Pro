package com.panwar2001.pdfpro.ui.view_models

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModel
import com.panwar2001.pdfpro.R
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.ImageType
import com.tom_roush.pdfbox.rendering.PDFRenderer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * Data class that represents the current UI state in terms of  and [uri]
 */
data class PdfToTextUiState(
    val uri: Uri=Uri.EMPTY,
    val isLoading:Boolean=false,
    val thumbnail: ImageBitmap= R.drawable.default_photo.toDrawable().toBitmap(width =300, height = 300).asImageBitmap(),
    val fileName: String="file.pdf"
)


class PdfToTextViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(PdfToTextUiState())
    val uiState: StateFlow<PdfToTextUiState> = _uiState.asStateFlow()
    /**
     * Set the [uri] of a file for the current ui state.
     */
    fun setUri(uri: Uri) {
        _uiState.update {
            it.copy(uri = uri)
        }
    }
    fun getUri():Uri{
        return uiState.value.uri
    }
    fun setLoading(isLoading:Boolean){
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }
    /**
     * Reset the order state
     */
    fun resetOrder() {
        _uiState.value = PdfToTextUiState()
    }

    /**
     * using pdf-box to generate thumbnail of pdf (Bitmap of first page of pdf using it's uri)
     * @param context application context
     */
//    @WorkerThread
    fun generateThumbnailFromPDF(context: Context?){
        Timer().schedule(1){
            val inputStream= context?.contentResolver?.openInputStream(uiState.value.uri)
            inputStream.use {
                val document= PDDocument.load(it)
                val renderer= PDFRenderer(document)
                val bitmap=renderer.renderImage(0,0.5F, ImageType.RGB)
                _uiState.update {state->
                    state.copy(thumbnail = bitmap.asImageBitmap()) }
                setLoading(false)
                document.close()
            }
            // Set File Name
            val returnCursor = context?.contentResolver?.query(uiState.value.uri, null, null, null, null)
            returnCursor.use {
                if (it != null) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    it.moveToFirst()
                    val fileName = it.getString(nameIndex)
                    it.close()
                    _uiState.update {state->
                        state.copy(fileName = fileName)
                    }
                }
            }
        }
    }
}

