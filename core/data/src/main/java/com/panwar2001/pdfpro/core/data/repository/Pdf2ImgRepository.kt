package com.panwar2001.pdfpro.core.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import com.panwar2001.pdfpro.view_models.PdfToImagesUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface Pdf2ImgRepository{
    val progress: StateFlow<Float>
    fun initPdfToImagesUiState(): PdfToImagesUiState
    suspend fun pdfToImages(uri: Uri): List<Bitmap>
}





//fun saveImageToStorage(uri: Uri,context:Context) {
//    val contentResolver = context.contentResolver
//
//    try {
//        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
//        val fileName = "my_image.jpg" // Replace with desired filename and extension
//        val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // Adjust compression as needed
//        outputStream.flush()
//        outputStream.close()
//        // Show success message or perform other actions
//    } catch (e: Exception) {
//        // Handle errors during saving
//        e.printStackTrace()
//    }
//}

