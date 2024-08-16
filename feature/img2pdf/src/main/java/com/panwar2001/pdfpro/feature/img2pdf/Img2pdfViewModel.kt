package com.panwar2001.pdfpro.feature.img2pdf

import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult.Page
import com.panwar2001.pdfpro.core.data.repository.Img2PdfRepository
import com.panwar2001.pdfpro.core.data.repository.ToolsRepository
import com.panwar2001.pdfpro.model.ImageInfo
import com.panwar2001.pdfpro.model.Img2PdfUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Img2pdfViewModel
@Inject
constructor(
    private val img2PdfRepository: Img2PdfRepository,
    private val toolsRepository: ToolsRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(img2PdfRepository.initImg2PdfUiState())
    val uiState: StateFlow<Img2PdfUiState> = _uiState.asStateFlow()

    val progress: StateFlow<Float> = img2PdfRepository.progress

    /**
     * Set the images for the current ui state.
     * @param uris list of uri of images
     */
    fun setUris(uris: List<Uri>,isDocScanUri: Boolean) {
        _uiState.update {
            it.copy(imageList = uris.map{ uri->img2PdfRepository.getImageInfo(uri = uri,isDocScanUri)})
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
    fun addImgUris(uris: List<Uri>,isDocScanUri: Boolean){
        _uiState.update {
            it.copy(imageList = it.imageList +  uris.map{ uri->img2PdfRepository.getImageInfo(uri = uri,isDocScanUri)})
        }
    }
    fun addDocScanUris(pages: List<Page>){

        _uiState.update {
            it.copy(imageList = it.imageList + pages.map { page->
                ImageInfo(page.imageUri,
                    "image/jpeg",
                    toolsRepository.getFileSize( page.imageUri.toFile().length())
                )})
        }
    }
    fun getFileSize(docUri: Uri){

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

    fun renamePdfFile(uri: Uri,newFileName:String){
        viewModelScope.launch {
            var newUri:Uri?=null
            var isSuccess=true
            try {
                newUri=img2PdfRepository.renamePdfFile(uri,"$newFileName.pdf")
            }catch (e: Exception){
                isSuccess=false
                e.printStackTrace()
            }finally {
                if(isSuccess && newUri!=null){
                    _uiState.update {
                        it.copy(fileUri = newUri,
                                fileName = newFileName)
                    }
                }
            }
        }
    }
}