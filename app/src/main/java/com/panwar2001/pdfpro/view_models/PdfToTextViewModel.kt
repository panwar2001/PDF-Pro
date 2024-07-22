package com.panwar2001.pdfpro.view_models

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.Pdf2TextRepository
import com.panwar2001.pdfpro.data.ToolsInterfaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Data class that represents the current UI state
 */
data class PdfToTextUiState(
    val uri: Uri,
    val isLoading:Boolean,
    val thumbnail: Bitmap,
    val pdfFileName: String,
    val textFileName: String,
    val text: String,
    val numPages:Int,
    val userMessage: Int?,
    val fileUniqueId: Long,
    val triggerSuccess: Boolean
)

@HiltViewModel
class PdfToTextViewModel
@Inject
constructor(private val toolsRepository: ToolsInterfaceRepository,
            private val pdf2TextRepository: Pdf2TextRepository): ViewModel() {

    private val _uiState = MutableStateFlow(pdf2TextRepository.initPdfToTextUiState())
    val uiState: StateFlow<PdfToTextUiState> = _uiState.asStateFlow()
    val allFilesInfo = pdf2TextRepository.getAllTextFiles()
    val snackBarHostState= SnackbarHostState()

    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun setPdfText(text: String) {
        _uiState.update { state ->
            state.copy(text = text)
        }
    }

    fun setTriggerSuccess(success: Boolean) {
        _uiState.update {
            it.copy(triggerSuccess = success)
        }
    }

    private fun setTextFileName(text: String) {
        _uiState.update { state ->
            state.copy(textFileName = text)
        }
    }
    private fun emitSnackBarMessage(message: String){
        viewModelScope.launch {
            snackBarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
        }
    }

    /**
     * using pdf-box to generate thumbnail of pdf (Bitmap of first page of pdf using it's uri)
     */

    fun pickPdf(uri: Uri) {
        var isSuccess = true
        var thumbnail = toolsRepository.getDefaultThumbnail()
        var numPages = 0
        var fileName = ""
        viewModelScope.launch {
            try {
                setLoading(true)
                thumbnail = toolsRepository.getThumbnailOfPdf(uri)
                numPages = toolsRepository.getNumPages(uri)
                fileName = toolsRepository.getPdfName(uri)
            } catch (e: Exception) {
                isSuccess = false
                emitSnackBarMessage(e.message?:"error")
                e.printStackTrace()
            } finally {
                if (isSuccess) {
                    emitSnackBarMessage("Success")
                    _uiState.update { state ->
                        state.copy(
                            thumbnail = thumbnail,
                            numPages = numPages,
                            pdfFileName = fileName,
                            uri = uri,
                            triggerSuccess = true
                        )
                    }
                }
                setLoading(false)
            }
        }
    }

    fun convertToText() {
        var isSuccess = true
        var text = ""
        var info = Pair(0L, "")
        viewModelScope.launch {
            try {
                setLoading(true)
                text = toolsRepository.convertToText(uri = uiState.value.uri)
                info = pdf2TextRepository.createTextFile(text, uiState.value.pdfFileName)
            } catch (e: Exception) {
                e.printStackTrace()
                isSuccess = false
                emitSnackBarMessage(e.message?:"error")
            } finally {
                if (isSuccess) {
                    emitSnackBarMessage("success")
                    setTriggerSuccess(true)
                    _uiState.update {
                        it.copy(
                            text = text,
                            fileUniqueId = info.first,
                            textFileName = info.second,
                            triggerSuccess = true
                        )
                    }
                }
                setLoading(false)
            }
        }
    }

    fun deleteFile(id: Long) {
        viewModelScope.launch {
            try {
                pdf2TextRepository.deleteTextFile(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun readTextFromFile(id: Long) {
        var text = ""
        var textFileName = ""

        viewModelScope.launch {
            var isSuccess = true
            try {
                setLoading(true)
                val fileDetails = pdf2TextRepository.getTextAndNameFromFile(id)
                text = fileDetails.first
                textFileName = fileDetails.second
            } catch (e: Exception) {
                e.printStackTrace()
                emitSnackBarMessage(e.message?:"error")
                isSuccess = false
            } finally {
                if (isSuccess) {
                    _uiState.update {
                        it.copy(
                            text = text,
                            textFileName = textFileName,
                            fileUniqueId = id,
                            triggerSuccess = true
                        )
                    }
                    setTriggerSuccess(true)
                    emitSnackBarMessage("success")
                }
                setLoading(false)
            }
        }
    }

    fun modifyFileName(name: String) {
        viewModelScope.launch {
            try {
                pdf2TextRepository.modifyName(uiState.value.fileUniqueId, "$name.txt")
                setTextFileName(name)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}