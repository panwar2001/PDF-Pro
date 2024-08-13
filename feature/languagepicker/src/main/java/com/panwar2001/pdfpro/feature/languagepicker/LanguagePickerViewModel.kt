package com.panwar2001.pdfpro.feature.languagepicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.core.data.repository.AppLocaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagePickerViewModel @Inject
constructor(
    private val appLocaleRepository: AppLocaleRepository
): ViewModel() {
    val currentLocale: StateFlow<LanguagePickerUiState> = appLocaleRepository
        .getAppLocale()
        .map<String, LanguagePickerUiState>(LanguagePickerUiState::Success)
        .onStart { emit(LanguagePickerUiState.Loading) }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LanguagePickerUiState.Loading
    )

    fun setAppLocale(localeTag: String) = viewModelScope.launch {
            appLocaleRepository.setAppLocale(localeTag)
    }
}


sealed interface LanguagePickerUiState {
    data object Loading : LanguagePickerUiState

    data class Success(val appLocale: String): LanguagePickerUiState
}
