package com.panwar2001.pdfpro.ui.view_models

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.core.content.FileProvider
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
 * represents the current UI state
 */
data class Img2PdfUiState(
    val imageList: List<ImageInfo> = listOf(),
    val isLoading:Boolean=false,
    val fileName: String="file",
    val progress:Float=0f,
    val fileUri:Uri=Uri.EMPTY,
    val numPages:Int=0
)

/**
 * Android view model is used to pass the context to the view model
 */
class Img2pdfViewModel(application: Application):AndroidViewModel(application ){
    private val _uiState = MutableStateFlow(Img2PdfUiState())
    val uiState: StateFlow<Img2PdfUiState> = _uiState.asStateFlow()
    /**
     * Set the images for the current ui state.
     * @param uris list of uri of images
     */
    fun setUris(uris: List<Uri>) {
        _uiState.update {
            it.copy(imageList = uris.map{ uri->getImageInfo(uri = uri)})
        }
    }

    fun setLoading(isLoading:Boolean){
        _uiState.update {it.copy(isLoading = isLoading)}
    }

    /**
     * Gets the image information- type and it's size from it's uri
     * DocumentFile is a wrapper over File and does query internally
     * on a list of [uri] to get it's data
     *
     *  @param uri of image
     */
    private fun getImageInfo(uri:Uri):ImageInfo{
        val context = getApplication<Application>().applicationContext
        val docFile=DocumentFile.fromSingleUri(context,uri)
        return ImageInfo(uri = uri,
                         type = docFile?.type?:"",
                         size = docFile?.length()?.toMB() ?: 0f)
    }

    /**
     * convert Bytes into MegaBytes
     */
    private fun Long.toMB():Float{
        return  Math.round(this*100.0f/1048576)/100f
    }

    /**
     * Reset the UI state
     */
    fun resetState() {
        _uiState.value = Img2PdfUiState()
    }

    /**
     * [move] Used in reorder screen to update position of images in imageList
     * @param fromIndex from where the image will be removed at
     * @param toIndex the index of imageList to where the image will be moved to
     */
    fun move(fromIndex: Int, toIndex: Int) {
        val imgList = _uiState.value.imageList.toMutableList()
        imgList.add(toIndex, imgList.removeAt(fromIndex))
        _uiState.update { it.copy(imageList = imgList) }
    }

    /**
     * Append a list of image [uris] data to imageList
     */
    fun addImgUris(uris: List<Uri>){
        _uiState.update {
            it.copy(imageList = it.imageList + uris.map(::getImageInfo))
        }
    }

    /**
     *
     */
    fun setCheckBoxState(index: Int, checkBoxState:Boolean){
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