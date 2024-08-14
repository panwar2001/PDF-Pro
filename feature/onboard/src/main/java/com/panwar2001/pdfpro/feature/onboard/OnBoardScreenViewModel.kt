package com.panwar2001.pdfpro.feature.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OnBoardScreenViewModel @Inject
    constructor(
    private val userDataRepository: UserDataRepository
    ): ViewModel(){
    /**
     * If onboarding is done once when app is run for first time then always
     * home screen is opened after splash screen.
     */
    fun setOnboardingFinished(){
        viewModelScope.launch {
            userDataRepository.setOnboardingDone()
        }
    }
}