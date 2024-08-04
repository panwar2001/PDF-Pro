package com.panwar2001.pdfpro.usecase

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LockPdfUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(uri: Uri):Boolean= withContext(Dispatchers.Default){
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")?: return@withContext false
        return@withContext try {
            PdfRenderer(parcelFileDescriptor)
            false
        } catch (securityException: SecurityException) {
            true
        }
    }
}