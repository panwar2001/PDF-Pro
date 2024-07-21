package com.panwar2001.pdfpro.view_models

import android.graphics.Bitmap
import android.net.Uri
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
 * Data class that represents the current UI state in terms of  and [uri]
 */
data class PdfToTextUiState(
    val uri: Uri,
    val isLoading:Boolean,
    val thumbnail: Bitmap,
    val fileName: String,
    val text: String,
    val numPages:Int,
    val userMessage: Int,
    val state: String,
    val fileUniqueId: Long
)

@HiltViewModel
class PdfToTextViewModel
@Inject
constructor(private val toolsRepository: ToolsInterfaceRepository,
            private val pdf2TextRepository: Pdf2TextRepository): ViewModel() {

    private val _uiState = MutableStateFlow(pdf2TextRepository.initPdfToTextUiState())
    val uiState: StateFlow<PdfToTextUiState> = _uiState.asStateFlow()

    val allFilesInfo=pdf2TextRepository.getAllTextFiles()
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

   private fun setFileName(text: String){
       _uiState.update { state ->
           state.copy(fileName = text)
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
                val text=toolsRepository.convertToText(uri = uiState.value.uri)
                val info= pdf2TextRepository.createTextFile(text,uiState.value.fileName)
                _uiState.update {
                    it.copy(
                        text=text,
                        fileUniqueId = info.first,
                        fileName = info.second
                    )
                }
            }catch (e: Exception){
                e.printStackTrace()
                state="error"
                setSnackBarMessage(R.string.error_message)
            }finally {
                setState(state)
                setLoading(false)
            }
        }
    }
    fun deleteFile(id: Long){
        viewModelScope.launch {
            try {
                pdf2TextRepository.deleteTextFile(id)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    fun readTextFromFile(id: Long){
        viewModelScope.launch {
            try {
                val fileDetails= pdf2TextRepository.getTextAndNameFromFile(id)
                _uiState.update {
                    it.copy(text = fileDetails.first,
                            fileName = fileDetails.second,
                            fileUniqueId = id)
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    fun modifyFileName(name: String){
        viewModelScope.launch {
            try {
                pdf2TextRepository.modifyName(uiState.value.fileUniqueId,name)
                setFileName(name)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}

