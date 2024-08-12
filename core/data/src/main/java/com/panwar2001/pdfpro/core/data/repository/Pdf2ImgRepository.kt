package com.panwar2001.pdfpro.data

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

interface Pdf2ImgInterface{
    val progress: StateFlow<Float>
    fun initPdfToImagesUiState(): PdfToImagesUiState
    suspend fun pdfToImages(uri: Uri): List<Bitmap>
}

@Singleton
class Pdf2ImgRepository @Inject constructor(@ApplicationContext private val context: Context) :Pdf2ImgInterface{
    private val _progress = MutableStateFlow(0f)
    override val progress: StateFlow<Float> get() = _progress

    override fun initPdfToImagesUiState(): PdfToImagesUiState {
        return PdfToImagesUiState(
            uri= Uri.EMPTY,
            isLoading=false,
            thumbnail= Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
            fileName="file.pdf",
            images= listOf(),
            numPages=0
        )
    }

    override suspend fun pdfToImages(uri: Uri): List<Bitmap> {
        val contentResolver = context.contentResolver
        val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")
        _progress.value=0f
        if(fileDescriptor==null){
            throw NullPointerException("File descriptor is null")
        }
        fileDescriptor.use { descriptor ->
            val pdfRenderer = PdfRenderer(descriptor)
            pdfRenderer.use { renderer ->
                val imagesList = mutableListOf<Bitmap>()
                for (pageNum in 0 until renderer.pageCount) {
                    val page = renderer.openPage(pageNum)
                    val bitmap = Bitmap.createBitmap(
                        page.width,
                        page.height,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    val paint = Paint().apply { color= Color.WHITE}
                    canvas.drawRect(0f, 0f, page.width.toFloat(), page.height.toFloat(), paint)

                    page.render(
                        bitmap,
                        null,
                        null,
                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                    )
                    imagesList.add(bitmap)
                    _progress.value=pageNum * 1.0f / renderer.pageCount
                    page.close()
                }
                return imagesList
            }
        }
    }

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

