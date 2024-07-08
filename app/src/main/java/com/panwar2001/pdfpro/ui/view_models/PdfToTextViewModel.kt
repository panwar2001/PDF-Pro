package com.panwar2001.pdfpro.ui.view_models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.provider.OpenableColumns
import androidx.annotation.WorkerThread
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModel
import com.panwar2001.pdfpro.R
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.schedule

/**
 * Data class that represents the current UI state in terms of  and [uri]
 */
data class PdfToTextUiState(
    val uri: Uri=Uri.EMPTY,
    val isLoading:Boolean=false,
    val thumbnail: ImageBitmap= R.drawable.default_photo.toDrawable().toBitmap(width =300, height = 300).asImageBitmap(),
    val fileName: String="file.pdf",
    val text: String = "",
    val numPages:Int=0
)

@HiltViewModel
class PdfToTextViewModel@Inject constructor(@ApplicationContext val context: Context): ViewModel() {
    private val _uiState = MutableStateFlow(PdfToTextUiState())
    val uiState: StateFlow<PdfToTextUiState> = _uiState.asStateFlow()
    /**
     * Set the [uri] of a file for the current ui state.
     */
    fun setUri(uri: Uri) {
        _uiState.update {
            it.copy(uri = uri,
                fileName = "",
                text="",
                numPages = 0)
        }
    }

    fun setLoading(isLoading:Boolean){
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }
    /**
     * Reset the order state
     */
    fun resetState() {
        _uiState.value = PdfToTextUiState()
    }

    /**
     * using pdf-box to generate thumbnail of pdf (Bitmap of first page of pdf using it's uri)
     */
    @WorkerThread
    fun generateThumbnailFromPDF(){
        try {
            Timer().schedule(1) {
                val contentResolver = context.contentResolver
                val fileDescriptor = contentResolver.openFileDescriptor(uiState.value.uri, "r")
                fileDescriptor.use { descriptor ->
                    val pdfRenderer = PdfRenderer(descriptor!!)
                    pdfRenderer.use { renderer ->
                        val page = renderer.openPage(0)
                        val bitmap = Bitmap.createBitmap(
                            page.width,
                            page.height,
                            Bitmap.Config.ARGB_8888
                        )
                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        _uiState.update { state ->
                            state.copy(
                                thumbnail = bitmap.asImageBitmap(),
                                numPages = pdfRenderer.pageCount
                            )
                        }
                        setLoading(false)
                        page.close()
                    }
                }
                //set file name
                val returnCursor =
                    context.contentResolver.query(uiState.value.uri, null, null, null, null)
                returnCursor.use {
                    if (it != null) {
                        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        it.moveToFirst()
                        val fileName = it.getString(nameIndex)
                        it.close()
                        _uiState.update { state ->
                            state.copy(fileName = fileName)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    @WorkerThread
     fun convertToText(){
        val inputStream=context.contentResolver.openInputStream(uiState.value.uri)
        Timer().schedule(1) {
            inputStream.use {
                val document = PDDocument.load(it)
                val textStripper = PDFTextStripper()
                _uiState.update { state ->
                    state.copy(text = textStripper.getText(document))
                }
                document.close()
                setLoading(false)
            }
        }
    }
}

