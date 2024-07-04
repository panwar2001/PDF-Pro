package com.panwar2001.pdfpro.ui.view_models

import android.app.Application
import android.content.ContentProvider
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.Page
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images.Media
import androidx.annotation.ContentView
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import androidx.core.graphics.decodeBitmap
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import com.panwar2001.pdfpro.data.ImageInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * Data class that represents the current UI state in terms of  and [uri]
 */
data class Img2PdfUiState(
    val imageList: List<ImageInfo> = listOf(),
    val isLoading:Boolean=false,
    val fileName: String="file",
    val progress:Float=0f,
    val fileUri:Uri=Uri.EMPTY,
    val numPages:Int=0
)

class Img2pdfViewModel(application: Application):AndroidViewModel(application ){
    private val _uiState = MutableStateFlow(Img2PdfUiState())
    val uiState: StateFlow<Img2PdfUiState> = _uiState.asStateFlow()
    /**
     * Set the [uris] of a file for the current ui state.
     */
    fun setUris(uris: List<Uri>) {
        _uiState.update {
            it.copy(imageList = uris.map{ uri->getImageInfo(uri = uri)},
                fileName = "",
                progress=0f
            )
        }
    }

    fun setLoading(isLoading:Boolean){
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }
    private fun getImageInfo(uri:Uri):ImageInfo{
        val context = getApplication<Application>().applicationContext
        val docFile=DocumentFile.fromSingleUri(context,uri)
        return ImageInfo(uri = uri,
                         type = docFile?.type?:"",
                         size = docFile?.length()?.toMB() ?: 0f)
    }
    private fun Long.toMB():Float{
        return  Math.round(this*100.0f/1048576)/100f
    }

    /**
     * Reset the order state
     */
    fun resetState() {
        _uiState.value = Img2PdfUiState()
    }
    fun move(fromIndex: Int, toIndex: Int) {
        val imgList = _uiState.value.imageList.toMutableList()
        imgList.add(toIndex, imgList.removeAt(fromIndex))
        _uiState.update { it.copy(imageList = imgList) }
    }
    fun addImgUris(uris: List<Uri>){
        val imgList = _uiState.value.imageList.toMutableList()
        imgList.addAll(uris.map{ uri->getImageInfo(uri = uri)})
        _uiState.update { it.copy(imageList = imgList) }
    }
    fun toggleCheckBox(index: Int,checkBoxState:Boolean){
        val imgList= _uiState.value.imageList.toMutableList()
        imgList[index]=imgList[index].copy(checked = checkBoxState)
        _uiState.update { it.copy(imageList = imgList ) }
    }
    fun deleteImages(){
        val imgList = _uiState.value.imageList.toMutableList()
        _uiState.update {
            it.copy(imageList = imgList.filter{imageInfo->!imageInfo.checked})
        }
    }
    @WorkerThread
    fun convert2Pdf(){
        Timer().schedule(1) {
            val document = PdfDocument()
            val context = getApplication<Application>().applicationContext
            val length = uiState.value.imageList.size
            for (i in 0..<length) {
                val uri = uiState.value.imageList[i].uri
                val bitmap = uri.toBitmap(context)
                val pageInfo = PageInfo.Builder(bitmap.width, bitmap.height, i + 1).create()
                val page = document.startPage(pageInfo)
                val canvas = page.canvas
                val paint = Paint()
                paint.color = Color.White
                canvas.drawBitmap(bitmap, 0f, 0f, null)
                document.finishPage(page)
                _uiState.update {
                    it.copy(progress = i * 1.0f / length)
                }
            }
            val newPdfFile = File(context.filesDir, "file.pdf")
            try {
                FileOutputStream(newPdfFile).use {
                    document.writeTo(it)
                    val uri: Uri = FileProvider
                        .getUriForFile(
                            context,
                            context.packageName + ".fileprovider",
                            newPdfFile
                        )
                    _uiState.update { state ->
                        state.copy(
                            fileUri = uri,
                            numPages = length
                        )
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            document.close()
            setLoading(false)
        }
    }
    private fun Uri.toBitmap(context: Context): Bitmap {
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

}