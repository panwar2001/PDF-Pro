package com.panwar2001.pdfpro.ui.view_models

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Data class that represents the current UI state in terms of [id] and [uri]
 */
data class PdfToImagesUiState(
    val uri: Uri=Uri.EMPTY,
    val isLoading:Boolean=false
)
class PdfToImagesViewModel:ViewModel(){
    private val _uiState = MutableStateFlow(PdfToImagesUiState())
    val uiState: StateFlow<PdfToImagesUiState> = _uiState.asStateFlow()
    /**
     * Set the [uri] of a file for the current ui state.
     */
    fun setUri(uri: Uri) {
        _uiState.update {
            it.copy(uri = uri)
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
    fun resetOrder() {
        _uiState.value = PdfToImagesUiState()
    }
}