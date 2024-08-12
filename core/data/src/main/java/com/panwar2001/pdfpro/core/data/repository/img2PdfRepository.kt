package com.panwar2001.pdfpro.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.panwar2001.pdfpro.view_models.Img2PdfUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

interface Img2PdfInterface{

    val progress: StateFlow<Float>

    fun initImg2PdfUiState(): Img2PdfUiState

    suspend fun savePdfToExternalStorage(externalStoragePdfUri:Uri,internalStoragePdfUri: Uri)

    suspend fun images2Pdf(imageList: List<ImageInfo>,maxHeight: Int=842, maxWidth: Int= 595): Uri

    suspend fun renamePdfFile(uri:Uri,newFileName: String):Uri
}

@Singleton
class Img2PdfRepository @Inject constructor(@ApplicationContext private val context: Context): Img2PdfInterface{

    private val _progress = MutableStateFlow(0f)
    override val progress: StateFlow<Float> get() = _progress

    override fun initImg2PdfUiState(): Img2PdfUiState{
        return Img2PdfUiState(
            imageList = listOf(),
            isLoading = false,
            fileName  = "PdfPro",
            fileUri   = Uri.EMPTY,
            numPages  = 0
        )
    }

    override suspend fun savePdfToExternalStorage(externalStoragePdfUri: Uri, internalStoragePdfUri: Uri) {
        val inputStream= File(context.filesDir, "file.pdf").inputStream()
        val buf = ByteArray(8192)
        var length: Int
        /**
         *  Sol: Reading input stream from file.pdf from app storage and  a buffer of 8KB is created.
         *  The write operation is done on output stream by writing 8KB of buffered data until
         *  the file ends.
         *  TODO
         *  the way to use internalStoragePdfUri didn't worked as the uri is  from
         *  FileProvider and it writing the data from it is generating a corrupted pdf on
         *  output stream. Find out a way to read and write pdf data from content uri generated
         *  from FileProvider to output stream.
         */
        context.contentResolver.apply {
            openOutputStream(externalStoragePdfUri,"w")?.use { outputStream->
                        while ((inputStream.read(buf).also { length = it }) != -1) {
                            outputStream.write(buf, 0, length)
                        }
            }
        }
    }

    private fun resizeBitmap(image: Bitmap, maxHeight: Int, maxWidth: Int): Bitmap {
        if (maxHeight > 0 && maxWidth > 0) {
            val sourceWidth: Int = image.width
            val sourceHeight: Int = image.height

            var targetWidth = maxWidth
            var targetHeight = maxHeight

            val sourceRatio = sourceWidth.toFloat() / sourceHeight.toFloat()
            val targetRatio = maxWidth.toFloat() / maxHeight.toFloat()

            if (targetRatio > sourceRatio) {
                targetWidth = (maxHeight.toFloat() * sourceRatio).toInt()
            } else {
                targetHeight = (maxWidth.toFloat() / sourceRatio).toInt()
            }

            return Bitmap.createScaledBitmap(
                image, targetWidth, targetHeight, true
            )

        } else {
            throw RuntimeException()
        }
    }
    @WorkerThread
    override suspend fun images2Pdf(imageList: List<ImageInfo>, maxHeight: Int, maxWidth: Int): Uri{
        val document = PdfDocument()
        val length = imageList.size
        _progress.value=0f
        for (i in 0..<length) {
            val uri = imageList[i].uri
            val bitmap = resizeBitmap(uri.toBitmap(),maxHeight,maxWidth)
            val pageInfo = PageInfo.Builder(maxWidth ,maxHeight, i + 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()
            paint.color = Color.WHITE
            canvas.drawBitmap(bitmap,
                (page.info.pageWidth-bitmap.width)/2.0f,
                (page.info.pageHeight-bitmap.height)/2.0f,
                null)
            document.finishPage(page)
            _progress.value=i * 1.0f / length
        }
        val newPdfFile = File(context.filesDir, "file.pdf")
        return withContext(Dispatchers.IO) {
            FileOutputStream(newPdfFile).use {
                document.writeTo(it)
                document.close()

                FileProvider.getUriForFile(context,
                    context.packageName + ".fileprovider",
                    newPdfFile)
            }
        }
    }
    private fun Uri.toBitmap(): Bitmap {
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(this)
            return BitmapFactory.decodeStream(inputStream)
                ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Return a fallback bitmap if decoding fails
        } catch (e: Exception) {
            e.printStackTrace()
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Return a fallback bitmap on any exception
        } finally {
            inputStream?.close()
        }
    }

    override suspend fun renamePdfFile(uri: Uri,newFileName: String):Uri {
        val name=uri.pathSegments.last()
        val pdfFile = File(context.filesDir, name)
        val newPdfFile=File(context.filesDir,newFileName)
        if(pdfFile.renameTo(newPdfFile)){
            return FileProvider.getUriForFile(context,
                context.packageName + ".fileprovider",
                newPdfFile)
        }else{
            throw Exception("Could not rename file")
        }
    }
}
