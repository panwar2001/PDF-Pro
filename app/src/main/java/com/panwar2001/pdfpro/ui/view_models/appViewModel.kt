package com.panwar2001.pdfpro.ui.view_models

import android.app.Application
import android.app.DownloadManager
import android.app.LocaleManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.LocaleList
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Data class that represents the current UI state
 */
data class AppUiState(
    val text:String=""
)


class AppViewModel(application: Application): AndroidViewModel(application ) {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
    /**
     * Reset the order state
     */
    fun resetState() {
        _uiState.value = AppUiState()
    }
    @WorkerThread
     fun getCurrentLocale(): String {
         val context = getApplication<Application>().applicationContext
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales.toLanguageTags()
        } else {
            AppCompatDelegate.getApplicationLocales().toLanguageTags()
        }
    }
    /**
     * TODO:  check for how to set locale for less than android 13 version
     */
    @WorkerThread
     fun setLocale(localeTag: String) {
        val context = getApplication<Application>().applicationContext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales=  LocaleList(
                Locale.forLanguageTag(localeTag))
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(
                    localeTag
                )
            )
        }
    }
    fun saveFileToDownload(uri:Uri,context: Context){
        val request = DownloadManager.Request(uri)
        request.setTitle("PDF Download") // Title of the download notification
        request.setDescription("Downloading PDF") // Description of the download notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        // Set destination in Downloads folder
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "example.pdf")

        // Enqueue the download and get download reference ID
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE)  as DownloadManager
        downloadManager.enqueue(request)
    }

}