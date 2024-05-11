package com.panwar2001.pdfpro.ui.view_models

import androidx.lifecycle.ViewModel
import com.panwar2001.pdfpro.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProViewModel :ViewModel(){
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

}