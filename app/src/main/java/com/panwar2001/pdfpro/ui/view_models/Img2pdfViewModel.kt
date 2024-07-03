package com.panwar2001.pdfpro.ui.view_models

import android.app.Application
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import com.panwar2001.pdfpro.data.ImageInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


/**
 * Data class that represents the current UI state in terms of  and [uri]
 */
data class Img2PdfUiState(
    val imageList: List<ImageInfo> = listOf(),
    val isLoading:Boolean=false,
    val fileName: String="file.pdf",
    val progress:Float=0f
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
}