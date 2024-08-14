package com.panwar2001.pdfpro

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.panwar2001.pdfpro.model.UserData
import com.panwar2001.pdfpro.screens.Screens
import com.panwar2001.pdfpro.ui.components.ProgressIndicator
import com.panwar2001.pdfpro.ui.theme.PDFProTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var uiState: AppUiState by mutableStateOf(AppUiState.Loading)

        // update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.userData
                    .onEach { uiState = it }
                    .collect()
            }
        }
        setContent {
            appContent(uiState)
        }
    }
    @Composable
    private fun appContent(uiState: AppUiState) {
            val darkTheme = shouldUseDarkTheme(uiState)
            PDFProTheme(darkTheme = darkTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(uiState is AppUiState.Success) {
                        NavigationController(
                            startDestination = getStartingDestination(uiState.userData)
                        )
                    }else{
                        ProgressIndicator(modifier = Modifier)
                        Text("onboarding screen loading..")
                    }
                }
            }
    }
    /**
     * If onboarding is done once when app is run for first time then always
     * home screen is opened after splash screen.
     */
    private fun getStartingDestination(
        userData: UserData
    ): String {
        userData.apply {
            return if(shouldHideOnboarding) Screens.Home.route else Screens.OnBoard.route
        }
    }
}

@Composable
private fun shouldUseDarkTheme(
    uiState: AppUiState
):Boolean = when(uiState){
    AppUiState.Loading -> false
    is AppUiState.Success -> uiState.userData.darkThemeEnabled
}
