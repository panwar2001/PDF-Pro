package com.panwar2001.pdfpro.core.domain

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConvertToTextUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
){
    suspend operator fun invoke(uri:Uri): String= withContext(Dispatchers.Default){
        PDFBoxResourceLoader.init(context)
        context.contentResolver.openInputStream(uri).use {
                PDDocument.load(it).use { doc ->
                    PDFTextStripper().getText(doc)
                }
            }
     }
}