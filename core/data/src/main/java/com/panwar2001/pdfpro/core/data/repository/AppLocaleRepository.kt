package com.panwar2001.pdfpro.core.data.repository

interface AppLocaleRepository {
    fun getAppLocale(): String

    suspend fun setAppLocale(localeTag: String)
}