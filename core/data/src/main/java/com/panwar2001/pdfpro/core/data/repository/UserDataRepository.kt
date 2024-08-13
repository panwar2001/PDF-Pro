package com.panwar2001.pdfpro.core.data.repository

import android.content.Context
import com.panwar2001.pdfpro.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun setDarkTheme()
    suspend fun setLightTheme()
    suspend fun setOnboardingDone()
    fun getAppVersion(): String
}
