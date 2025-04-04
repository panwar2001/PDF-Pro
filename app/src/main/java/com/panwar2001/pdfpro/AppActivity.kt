package com.panwar2001.pdfpro

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.panwar2001.pdfpro.core.ui.theme.LocalBackgroundTheme
import com.panwar2001.pdfpro.core.ui.theme.PDFProTheme
import com.panwar2001.pdfpro.model.UserData
import com.panwar2001.pdfpro.screens.Screens
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private val viewModel: AppViewModel by viewModels()
    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableStrictModeOnDebug()

        checkUpdate()

        var uiState: AppUiState by mutableStateOf(AppUiState.Loading)

        /**
         * update the uiState
         */
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userData
                    .onEach { uiState = it }
                    .collect()
            }
        }

        /**
         *  Turn off the decor fitting system windows, which allows us to handle insets,
         *  including IME animations, and go edge-to-edge This also sets up the initial
         *  system bar style based on the platform theme
         */
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { uiState is AppUiState.Loading }
        setContent {
            Content(uiState)
        }
    }

    private fun checkUpdate(){
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                requestUpdate(appUpdateInfo)
             }
        }
    }
    private fun requestUpdate(appUpdateInfo: AppUpdateInfo){
        val updateActivityLauncher= registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result: ActivityResult ->
            // handle callback
            if (result.resultCode != RESULT_OK) {
                error("Update flow failed! Result code: " + result.resultCode)
                // If the update is canceled or fails,
                // you can request to start the update again.
            }
        }

        // Request the update.
        appUpdateManager.startUpdateFlowForResult(
            // Pass the intent that is returned by 'getAppUpdateInfo()'.
            appUpdateInfo,
            // an activity result launcher registered via registerForActivityResult
            updateActivityLauncher,
            // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
            // flexible updates.
            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build())
    }
    /**
     * Checks that the update is not installed during 'onResume()'.
     * However, you should execute this check at all entry points into the app.
     */
    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo->
            // If an in-app update is already running, resume the update.
            if(appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                requestUpdate(appUpdateInfo)
            }
        }
    }
    @Composable
    private fun Content(uiState: AppUiState) {
        val darkTheme = shouldUseDarkTheme(uiState)

        EnableEdgeToEdge(darkTheme = darkTheme)

        PDFProTheme(darkTheme = darkTheme) {
            /**
             *  A surface container using the 'background' color from the theme
             */
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = LocalBackgroundTheme.current.color,
                tonalElevation = LocalBackgroundTheme.current.tonalElevation
            ) {
                if (uiState is AppUiState.Success) {
                    NavigationController(
                        startDestination = getStartingDestination(uiState.userData)
                    )
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
            return if (shouldHideOnboarding) Screens.Home.route else Screens.OnBoard.route
        }
    }

    /**
     * Update the edge to edge configuration to match the theme
     * This is the same parameters as the default enableEdgeToEdge call, but we manually
     * resolve whether or not to show dark theme using uiState, since it can be different
     * than the configuration's dark theme value based on the user preference.
     */
    @Composable
    private fun EnableEdgeToEdge(darkTheme: Boolean) {
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
    }

    /**
     * Identify expensive operations on the main thread
     *
     * Using StrictMode.ThreadPolicy enables the thread policy on all debug builds
     * and crashes the app whenever violations of the thread policy are detected,
     * which makes it difficult to miss thread policy violations.
     *
     * source: https://developer.android.com/topic/performance/appstartup/analysis-optimization#kotlin
     *
     * TODO("THERE IS DISK READ ON MAIN THREAD DUE TO PREFERENCES DATASTORE ,
     *   THUS permitDiskRead() IS APPLIED FOR NOW WHICH MUST BE REMOVED,
     *   APP MUST FUNCTION PROPERLY , NO DISK READ ON MAIN THREAD")
     */
    private fun enableStrictModeOnDebug() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy
                    .Builder()
                    .detectAll()
                    .penaltyDeath()
                    .permitDiskReads()
                    .build()
            )
        }
    }
}

@Composable
private fun shouldUseDarkTheme(
    uiState: AppUiState
): Boolean = when (uiState) {
    AppUiState.Loading -> false
    is AppUiState.Success -> uiState.userData.darkThemeEnabled
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private const val ALPHA_LIGHT = 0xE6
private const val RED_LIGHT = 0xFF
private const val GREEN_LIGHT = 0xFF
private const val BLUE_LIGHT = 0xFF

private val lightScrim = android.graphics.Color.argb(ALPHA_LIGHT, RED_LIGHT, GREEN_LIGHT, BLUE_LIGHT)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private const val ALPHA_DARK = 0x80
private const val RED_DARK = 0x1B
private const val GREEN_DARK = 0x1B
private const val BLUE_DARK = 0x1B
private val darkScrim = android.graphics.Color.argb(ALPHA_DARK, RED_DARK, GREEN_DARK, BLUE_DARK)
