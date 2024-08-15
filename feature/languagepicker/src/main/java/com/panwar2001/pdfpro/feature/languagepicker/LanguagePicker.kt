package com.panwar2001.pdfpro.feature.languagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.panwar2001.pdfpro.ui.components.NavigationActions
import com.panwar2001.pdfpro.ui.components.ProgressIndicator

@Composable
fun LanguagePicker( viewModel: LanguagePickerViewModel = hiltViewModel(),
                    navActions: NavigationActions){
    val screenState by viewModel.currentLocale.collectAsState()
    val languages = rememberSaveable {
        listOf(
            R.array.english,
            R.array.French,
            R.array.Japanese,
            R.array.Russian,
            R.array.hindi
        )
    }
    when (screenState) {
        LanguagePickerUiState.Loading -> ProgressIndicator(modifier = Modifier)
        is LanguagePickerUiState.Success -> {
            LanguagePickerScreen(
                navigateUp = { navActions.navigateBack() },
                languages = languages,
                currentLocale = (screenState as LanguagePickerUiState.Success).appLocale,
                setLocale = viewModel::setAppLocale
            )
        }
    }
}