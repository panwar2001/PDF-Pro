package com.panwar2001.pdfpro.view_models

import android.net.Uri
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.data.ImageInfo
import com.panwar2001.pdfpro.data.Img2PdfInterface
import com.panwar2001.pdfpro.data.ToolsInterface
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
    val imageList: List<ImageInfo> ,
    val isLoading:Boolean,
    val fileName: String,
    val fileUri:Uri,
    val numPages:Int
)

@HiltViewModel
class Img2pdfViewModel @Inject constructor(private val toolsRepository: ToolsInterface,
                                           private val img2PdfRepository: Img2PdfInterface): ViewModel() {
    private val _uiState = MutableStateFlow(img2PdfRepository.initImg2PdfUiState())
    val uiState: StateFlow<Img2PdfUiState> = _uiState.asStateFlow()

    val progress: StateFlow<Float> = img2PdfRepository.progress

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
                 setLoading(true)
                 _uiState.update {
                     it.copy(
                         fileUri = img2PdfRepository.images2Pdf(uiState.value.imageList)
                     )
                 }
             }catch (e: Exception){
                 e.printStackTrace()
             }finally {
                 Log.e("tag",uiState.value.fileUri.toString())
                 setLoading(false)
             }
         }
    }

    fun savePdfToExternalStorage(externalStoragePdfUri:Uri,internalStoragePdfUri: Uri){
        viewModelScope.launch {
            try{
                setLoading(true)
                img2PdfRepository.savePdfToExternalStorage(externalStoragePdfUri,internalStoragePdfUri)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                setLoading(false)
            }
        }
    }
}