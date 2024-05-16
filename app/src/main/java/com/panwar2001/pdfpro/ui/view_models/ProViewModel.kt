package com.panwar2001.pdfpro.ui.view_models

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.panwar2001.pdfpro.data.PdfProUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProViewModel :ViewModel(){
    private val _uiState = MutableStateFlow(PdfProUiState())
    val uiState: StateFlow<PdfProUiState> = _uiState.asStateFlow()
    /**
     * Set the [uri] of a file for the current ui state.
     */
    fun setUri(uri: Uri) {
        _uiState.update { currentState ->
            currentState.copy(uri = uri)
        }
    }
    /**
     * Reset the order state
     */
    fun resetOrder() {
        _uiState.value = PdfProUiState()
    }

}