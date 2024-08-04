package com.panwar2001.pdfpro.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPdfThumbnailUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
suspend operator fun invoke(uri:Uri): Bitmap =withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")
            fileDescriptor.use { descriptor ->
                if(descriptor==null){
                    throw NullPointerException("file descriptor is null")
                }
                PdfRenderer(descriptor).use { renderer ->
                    val page = renderer.openPage(0)
                    val bitmap = Bitmap.createBitmap(
                        page.width,
                        page.height,
                        Bitmap.Config.ARGB_8888
                    )
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    page.close()
                    bitmap
                }
            }
      }
}
