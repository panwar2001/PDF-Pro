package com.panwar2001.pdfpro.core.domain

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class IsPdfCorruptedUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(uri: Uri):Boolean{
        return try {
            val descriptor = context.contentResolver.openFileDescriptor(uri, "r")
            descriptor?.use {
                PdfRenderer(it)
            }
            false
        } catch (e: IOException) {
            true
        }catch (e:Exception){
            return false
        }
    }
}