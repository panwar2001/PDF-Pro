package com.panwar2001.pdfpro.usecase

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetPdfPageCountUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(uri:Uri): Int {
        context.contentResolver.openFileDescriptor(uri, "r").use {
            if (it == null) return 0
            PdfRenderer(it).use { renderer ->
                return renderer.pageCount
            }
        }
    }
}