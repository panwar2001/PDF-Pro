package com.panwar2001.pdfpro.core.data.repository

import kotlinx.coroutines.flow.Flow

interface AppLocaleRepository {
    fun getAppLocale(): String

    suspend fun setAppLocale(localeTag: String)
}