package com.panwar2001.pdfpro.core.domain

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFileNameUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(uri:Uri): String= withContext(Dispatchers.Default){
        context.contentResolver
            .query(uri, null, null, null, null)
            ?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.moveToFirst()
                val fileName = it.getString(nameIndex)
                it.close()
                return@withContext fileName
            }
        throw NullPointerException("Could not get pdf file name")
    }
}