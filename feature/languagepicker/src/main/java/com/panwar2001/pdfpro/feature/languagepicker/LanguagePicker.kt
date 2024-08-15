package com.panwar2001.pdfpro.feature.languagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import com.panwar2001.pdfpro.core.ui.components.NavigationActions

@Composable
fun LanguagePicker( viewModel: LanguagePickerViewModel = hiltViewModel(),
                    navActions: NavigationActions
){
    val screenState by viewModel.uiState.collectAsState()
    val languages = rememberSaveable {
        listOf(
            R.array.english,
            R.array.French,
            R.array.Japanese,
            R.array.Russian,
            R.array.hindi
        )
    }
            LanguagePickerScreen(
                navigateUp = { navActions.navigateBack() },
                languages = languages,
                currentLocale = screenState.appLocale,
                setLocale = viewModel::setAppLocale
            )
}