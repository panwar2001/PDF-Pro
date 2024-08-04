package com.panwar2001.pdfpro.usecase

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConvertToTextUseCase @Inject constructor(
    @ApplicationContext private val context: Context
){
    suspend operator fun invoke(uri:Uri): String= withContext(Dispatchers.Default){
            context.contentResolver.openInputStream(uri).use {
                PDDocument.load(it).use { doc ->
                    PDFTextStripper().getText(doc)
                }
            }
     }
}