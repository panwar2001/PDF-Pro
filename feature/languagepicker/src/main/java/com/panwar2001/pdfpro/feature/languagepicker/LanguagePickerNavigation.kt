package com.panwar2001.pdfpro.feature.languagepicker

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.panwar2001.pdfpro.screens.Screens
import com.panwar2001.pdfpro.ui.components.NavigationActions

fun NavGraphBuilder.languagePickerNode(navActions: NavigationActions){
    composable(route= Screens.Home.LanguagePickerScreen.route){
        val viewModel= hiltViewModel<LanguagePickerViewModel>()
        val screenState by viewModel.currentLocale.collectAsState()
        LanguagePicker(
            navActions = navActions,
            screenState=screenState,
            setLocale = viewModel::setAppLocale
        )
    }
}