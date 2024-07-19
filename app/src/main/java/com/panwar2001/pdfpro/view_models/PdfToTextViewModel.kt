package com.panwar2001.pdfpro.view_models

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.ToolsInterfaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Data class that represents the current UI state in terms of  and [uri]
 */
data class PdfToTextUiState(
    val uri: Uri=Uri.EMPTY,
    val isLoading:Boolean=false,
    val thumbnail: Bitmap,
    val fileName: String="file.pdf",
    val text: String= "",
    val numPages:Int=0,
    val userMessage: Int=0,
    val state: String=""
)

@HiltViewModel
class PdfToTextViewModel
@Inject
constructor(private val toolsRepository: ToolsInterfaceRepository): ViewModel() {

    private val _uiState = MutableStateFlow(PdfToTextUiState(
        thumbnail = toolsRepository.getDefaultThumbnail()
    ))
    val uiState: StateFlow<PdfToTextUiState> = _uiState.asStateFlow()
    /**
     * Set the [uri] of a file for the current ui state.
     */
    fun setUri(uri: Uri) {
        _uiState.update {
            it.copy(
                uri = uri,
                fileName = "",
                text = "",
                numPages = 0
            )
        }
    }
    fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun setState(state:String){
        _uiState.update {
            it.copy(state = state)
        }
    }
     fun setSnackBarMessage(message: Int) {
        _uiState.update {
            it.copy(userMessage = message)
        }
    }
    fun setPdfText(text: String){
        _uiState.update { state ->
            state.copy(text = text)
        }
    }


    /**
     * using pdf-box to generate thumbnail of pdf (Bitmap of first page of pdf using it's uri)
     */

    fun generateThumbnailFromPDF() {
        var state="success"
        viewModelScope.launch {
            try {
                _uiState.update { state ->
                    state.copy(
                        thumbnail =toolsRepository.getThumbnailOfPdf(uiState.value.uri),
                        numPages = toolsRepository.getNumPages(uiState.value.uri),
                        fileName = toolsRepository.getPdfName(uiState.value.uri)
                    )
                }
            }
    catch (e: Exception){
        state="error"
        e.printStackTrace()
        setSnackBarMessage(R.string.error_message)
    }
    finally {
        setState(state)
        setLoading(false)
    }
     }
    }

     fun convertToText(){
        var state="success"
        viewModelScope.launch {
            try {
                setPdfText(toolsRepository.convertToText(uri = uiState.value.uri))
            }catch (e: Exception){
                e.printStackTrace()
                state="failure"
                setSnackBarMessage(R.string.error_message)
            }finally {
                setState(state)
                setLoading(false)
            }
        }
    }
}

