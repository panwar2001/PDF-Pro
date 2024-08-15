package com.panwar2001.pdfpro.feature.languagepicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.core.data.repository.AppLocaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LanguagePickerUiState(
    val appLocale: String="en"
)
@HiltViewModel
class LanguagePickerViewModel @Inject
constructor(
    private val appLocaleRepository: AppLocaleRepository
): ViewModel() {
    private val _uiState= MutableStateFlow(LanguagePickerUiState())
    val uiState = _uiState.asStateFlow()
    init {
        updateLocale(appLocaleRepository.getAppLocale())
    }
    private fun updateLocale(locale: String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(appLocale = locale)
            }
        }
    }
    fun setAppLocale(localeTag: String) {
        viewModelScope.launch {
            appLocaleRepository.setAppLocale(localeTag)
            updateLocale(localeTag)
        }
    }
}