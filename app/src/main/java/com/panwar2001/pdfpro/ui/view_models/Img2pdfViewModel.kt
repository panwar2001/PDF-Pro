package com.panwar2001.pdfpro.ui.view_models

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Data class that represents the current UI state in terms of  and [uri]
 */
data class Img2PdfUiState(
    val Uris: List<Uri> = listOf(),
    val isLoading:Boolean=false,
    val fileName: String="file.pdf",
    val progress:Float=0f
)


class Img2pdfViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(Img2PdfUiState())
    val uiState: StateFlow<Img2PdfUiState> = _uiState.asStateFlow()
    /**
     * Set the [uris] of a file for the current ui state.
     */
    fun setUris(uris: List<Uri>) {
        _uiState.update {
            it.copy(Uris = uris,
                fileName = "",
                progress=0f
            )
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
        _uiState.value = Img2PdfUiState()
    }

}