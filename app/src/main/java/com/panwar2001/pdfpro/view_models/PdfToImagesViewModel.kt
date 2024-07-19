package com.panwar2001.pdfpro.view_models

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
data class PdfToImagesUiState(
    val uri: Uri=Uri.EMPTY,
    val isLoading:Boolean=false,
    val thumbnail: Bitmap,
    val fileName: String="file.pdf",
    val images:List<Bitmap> = listOf(),
    val numPages:Int=0
)

@HiltViewModel
class PdfToImagesViewModel
@Inject
constructor(private val toolsRepository: ToolsInterfaceRepository): ViewModel() {
    private val _uiState = MutableStateFlow(PdfToImagesUiState(
        thumbnail = toolsRepository.getDefaultThumbnail()
    ))
    val uiState: StateFlow<PdfToImagesUiState> = _uiState.asStateFlow()

    val progress: StateFlow<Float> = toolsRepository.progress

    /**
     * Set the [uri] of a file for the current ui state.
     */
    fun setUri(uri: Uri) {
        _uiState.update {
            it.copy(uri = uri,
                fileName = "",
                images= listOf(),
                numPages = 0
            )
        }
    }

  fun setLoading(isLoading:Boolean){
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    @WorkerThread
    fun generateThumbnailFromPDF(){
        try {
            viewModelScope.launch {
                _uiState.update { state ->
                    state.copy(
                        thumbnail =toolsRepository.getThumbnailOfPdf(uiState.value.uri),
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
    fun generateImages(){
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(images = toolsRepository.pdfToImages(uiState.value.uri))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                setLoading(false)
            }
        }
    }
}