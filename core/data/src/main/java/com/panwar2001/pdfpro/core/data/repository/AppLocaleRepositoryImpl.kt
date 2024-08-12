package com.panwar2001.pdfpro.core.data.repository

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class AppLocaleRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
):AppLocaleRepository {
    // TODO("check for how to set locale for less than android 13 version")
    override fun getAppLocale(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales.toLanguageTags()
        } else {
            AppCompatDelegate.getApplicationLocales().toLanguageTags()
        }
    }
    //TODO("check for how to set locale for less than android 13 version")
    override suspend fun setAppLocale(localeTag: String) {
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
}