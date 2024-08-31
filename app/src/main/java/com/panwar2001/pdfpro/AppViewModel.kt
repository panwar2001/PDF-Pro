package com.panwar2001.pdfpro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.core.data.repository.UserDataRepository
import com.panwar2001.pdfpro.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel
@Inject
constructor(userDataRepository: UserDataRepository) : ViewModel() {
    val userData = userDataRepository
        .userData
        .onStart { AppUiState.Loading }
        .map { AppUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppUiState.Loading
        )
}

sealed interface AppUiState {
    data object Loading : AppUiState
    data class Success(val userData: UserData) : AppUiState
}
