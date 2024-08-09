package com.panwar2001.pdfpro

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.MobileAds
import com.panwar2001.pdfpro.navigation.NavigationController
import com.panwar2001.pdfpro.navigation.Screens
import com.panwar2001.pdfpro.theme.PDFProTheme
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PDFBoxResourceLoader.init(applicationContext)
            pdfProContent()
    }

    private fun pdfProContent() {
        setContent {
            val theme = remember {
                mutableStateOf(isDarkTheme())
            }

                PDFProTheme(darkTheme = theme.value) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavigationController(
                            startDestination = getStartDestination(),
                            setOnboardingFinished = {
                                this.getSharedPreferences("onBoarding", MODE_PRIVATE)
                                    .edit().putBoolean("isFinished", true).apply()
                            },
                            setTheme = {
                                val sharedPreferences =
                                    this.getSharedPreferences("Theme", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putBoolean("theme", it)
                                editor.apply()
                                theme.value = it
                            },
                            currentTheme = theme.value
                        )
                    }
                }
            }
        }
    /**
     * If onboarding is done once when app is run for first time then always
     * home screen is opened after splash screen.
     */
    private fun getStartDestination( ): String {
        val sharedPreferences = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val onBoardingIsFinished= sharedPreferences.getBoolean("isFinished", false)
        return if(onBoardingIsFinished) Screens.Home.route else Screens.OnBoard.route
    }
    private fun isDarkTheme():Boolean{
        val sharedPreferences = this.getSharedPreferences("Theme", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("theme", false)
    }
}

