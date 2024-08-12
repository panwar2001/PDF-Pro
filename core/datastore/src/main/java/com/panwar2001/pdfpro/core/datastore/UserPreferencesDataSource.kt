package com.panwar2001.pdfpro.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>,
) {
    companion object {
        val ONBOARDING_STATE = booleanPreferencesKey("onboardingState")
    }

    suspend fun onBoardingDone(){
        userPreferences.edit { preferences->
            preferences[ONBOARDING_STATE] = true
        }
    }

    fun isOnboardingCompleted()= userPreferences.data.map { preferences->
        preferences[ONBOARDING_STATE]?:false
    }
}