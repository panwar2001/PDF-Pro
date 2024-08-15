package com.panwar2001.pdfpro.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject
constructor(
    private val userDataRepository: UserDataRepository
):ViewModel(){
    val settings: StateFlow<SettingsUiState> = userDataRepository
        .userData
        .map{
            val appVersion= userDataRepository.getAppVersion()
            SettingsUiState.Success(it.darkThemeEnabled, appVersion)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading
        )

    fun setTheme(themeState: Boolean){
        viewModelScope.launch {
            userDataRepository.apply {
                if(themeState)
                    setLightTheme()
                else
                    setDarkTheme()
            }
        }
    }
}


sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(
        val isDarkTheme:Boolean,
        val appVersion: String
    ) : SettingsUiState
}