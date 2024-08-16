package com.panwar2001.pdfpro

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.panwar2001.pdfpro.model.UserData
import com.panwar2001.pdfpro.screens.Screens
import com.panwar2001.pdfpro.core.ui.components.ProgressIndicator
import com.panwar2001.pdfpro.core.ui.theme.PDFProTheme
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

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations, and go edge-to-edge
        // This also sets up the initial system bar style based on the platform theme
        enableEdgeToEdge()

        setContent {
            Content(uiState)
        }
    }
    @Composable
    private fun Content(uiState: AppUiState) {
        val darkTheme = shouldUseDarkTheme(uiState)
        // Update the edge to edge configuration to match the theme
        // This is the same parameters as the default enableEdgeToEdge call, but we manually
        // resolve whether or not to show dark theme using uiState, since it can be different
        // than the configuration's dark theme value based on the user preference.
        DisposableEffect(darkTheme) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                ) { darkTheme },
                navigationBarStyle = SystemBarStyle.auto(
                    lightScrim,
                    darkScrim,
                ) { darkTheme },
            )
            onDispose {}
        }

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



/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)