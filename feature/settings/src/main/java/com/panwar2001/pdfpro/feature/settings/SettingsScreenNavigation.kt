package com.panwar2001.pdfpro.feature.settings

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.panwar2001.pdfpro.feature.languagepicker.LanguagePicker
import com.panwar2001.pdfpro.screens.Screens
import com.panwar2001.pdfpro.core.ui.components.NavigationActions
import com.panwar2001.pdfpro.core.ui.components.ProgressIndicator

fun NavGraphBuilder.settingsScreenNavigation(navActions: NavigationActions){
    navigation(startDestination = Screens.Settings.SettingsScreen.route, route= Screens.Settings.route) {
        composable(route = Screens.Settings.SettingsScreen.route) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            val settings by viewModel.settings.collectAsState()
            if (settings is SettingsUiState.Loading) {
                ProgressIndicator(modifier = Modifier)
            } else if (settings is SettingsUiState.Success) {
                SettingScreen(
                    settings = settings as SettingsUiState.Success,
                    setTheme = viewModel::setTheme,
                    navigateTo = navActions::navigateTo,
                    navigateUp = navActions::navigateBack
                )
            }
        }
        composable(route = Screens.Settings.LanguagePickerScreen.route){
           LanguagePicker(navActions = navActions)
        }
    }
}