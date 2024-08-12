package com.panwar2001.pdfpro.core.data.repository

import com.panwar2001.pdfpro.core.datastore.UserPreferencesDataSource
import com.panwar2001.pdfpro.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AppSettingsRepository {
    val userData: Flow<UserData>
    suspend fun setDarkTheme()
    suspend fun setLightTheme()
    suspend fun setOnboardingDone()
}

class AppSettingsRepositoryImpl @Inject constructor(
    private val preferencesDataSource: UserPreferencesDataSource
) : AppSettingsRepository {
    override val userData: Flow<UserData> = preferencesDataSource.userData

    override suspend fun setOnboardingDone() {
        preferencesDataSource.onBoardingDone()
    }

    override suspend fun setLightTheme() {
       preferencesDataSource.setTheme(true)
    }

    override suspend fun setDarkTheme() {
        preferencesDataSource.setTheme(false)
    }
}