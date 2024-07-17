package com.panwar2001.pdfpro.view_models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.provider.OpenableColumns
import androidx.annotation.WorkerThread
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.ToolsRepository
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.schedule

/**
 * Data class that represents the current UI state in terms of  and [uri]
 */
data class PdfToTextUiState(
    val uri: Uri=Uri.EMPTY,
    val isLoading:Boolean=false,
    val thumbnail: ImageBitmap= R.drawable.default_photo.toDrawable().toBitmap(width =300, height = 300).asImageBitmap(),
    val fileName: String="file.pdf",
    val text: String = "",
    val numPages:Int=0
)

@HiltViewModel
class PdfToTextViewModel
@Inject
constructor(@ApplicationContext val context: Context,
            private val toolsRepository: ToolsRepository): ViewModel() {

    private val _uiState = MutableStateFlow(PdfToTextUiState())
    val uiState: StateFlow<PdfToTextUiState> = _uiState.asStateFlow()
    /**
     * Set the [uri] of a file for the current ui state.
     */
    fun setUri(uri: Uri) {
        _uiState.update {
            it.copy(uri = uri,
                fileName = "",
                text="",
                numPages = 0)
        }
    }

    fun setLoading(isLoading:Boolean){
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }
    /**
     * Reset the order state
     */
    fun resetState() {
        _uiState.value = PdfToTextUiState()
    }

    /**
     * using pdf-box to generate thumbnail of pdf (Bitmap of first page of pdf using it's uri)
     */
    @WorkerThread
    fun generateThumbnailFromPDF(){
        try {
            viewModelScope.launch {
                _uiState.update { state ->
                    state.copy(
                        thumbnail =toolsRepository.getThumbnailOfPdf(uiState.value.uri)!!,
                        numPages = toolsRepository.getNumPages(uiState.value.uri),
                        fileName = toolsRepository.getPdfName(uiState.value.uri)
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            setLoading(false)
        }
    }
    @WorkerThread
     fun convertToText(){
         try {
             viewModelScope.launch {
                 _uiState.update { state ->
                     state.copy(text = toolsRepository.convertToText(uri = uiState.value.uri))
                 }
             }
         }catch (e: Exception){
             e.printStackTrace()
         }finally {
             setLoading(false)
         }
    }
}

