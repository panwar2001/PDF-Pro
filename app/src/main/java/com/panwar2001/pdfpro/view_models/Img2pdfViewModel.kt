package com.panwar2001.pdfpro.view_models

import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.data.ImageInfo
import com.panwar2001.pdfpro.data.ToolsInterfaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * represents the current UI state
 */
data class Img2PdfUiState(
    val imageList: List<ImageInfo> = listOf(),
    val isLoading:Boolean=false,
    val fileName: String="file",
    val fileUri:Uri=Uri.EMPTY,
    val numPages:Int=0
)

@HiltViewModel
class Img2pdfViewModel @Inject constructor(private val toolsRepository: ToolsInterfaceRepository): ViewModel() {
    private val _uiState = MutableStateFlow(Img2PdfUiState())
    val uiState: StateFlow<Img2PdfUiState> = _uiState.asStateFlow()

    val progress: StateFlow<Float> = toolsRepository.progress

    /**
     * Set the images for the current ui state.
     * @param uris list of uri of images
     */
    fun setUris(uris: List<Uri>) {
        _uiState.update {
            it.copy(imageList = uris.map{ uri->toolsRepository.getImageInfo(uri = uri)})
        }
    }

    fun setLoading(isLoading:Boolean){
        _uiState.update {it.copy(isLoading = isLoading)}
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
            it.copy(imageList = it.imageList +  uris.map{ uri->toolsRepository.getImageInfo(uri = uri)})
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
         viewModelScope.launch {
             try {
                 _uiState.update {
                     it.copy(
                         fileUri = toolsRepository.images2Pdf(uiState.value.imageList)
                     )
                 }
             }catch (e: Exception){
                 e.printStackTrace()
             }finally {
                 setLoading(false)
             }
         }
    }
}