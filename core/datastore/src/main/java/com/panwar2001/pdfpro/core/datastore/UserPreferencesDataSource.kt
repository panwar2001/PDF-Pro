package com.panwar2001.pdfpro.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.panwar2001.pdfpro.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>,
) {
    companion object {
        val ONBOARDING_STATE = booleanPreferencesKey("onboardingState")
        val THEME_STATE = booleanPreferencesKey("themeState")
    }

    val userData = userPreferences.data
        .map {
            UserData(
                darkThemeEnabled = it[THEME_STATE]?:false,
                shouldHideOnboarding = it[ONBOARDING_STATE]?:false
            )
        }
    suspend fun onBoardingDone(){
        userPreferences.edit { preferences->
            preferences[ONBOARDING_STATE] = true
        }
    }
    suspend fun setTheme(themeState: Boolean){
        userPreferences.edit { preferences->
            preferences[THEME_STATE] = themeState
        }
    }
}