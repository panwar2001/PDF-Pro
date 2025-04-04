package com.panwar2001.pdfpro.feature.pdf2txt

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.data.Pdf2TextInterface
import com.panwar2001.pdfpro.data.ToolsInterface
import com.panwar2001.pdfpro.core.domain.ConvertToTextUseCase
import com.panwar2001.pdfpro.core.domain.EventType
import com.panwar2001.pdfpro.core.domain.GetFileNameUseCase
import com.panwar2001.pdfpro.core.domain.GetPdfPageCountUseCase
import com.panwar2001.pdfpro.core.domain.GetPdfThumbnailUseCase
import com.panwar2001.pdfpro.core.domain.IsPdfCorruptedUseCase
import com.panwar2001.pdfpro.core.domain.IsPdfLockedUseCase
import com.panwar2001.pdfpro.core.domain.UiEventUseCase
import com.panwar2001.pdfpro.core.domain.UnlockPdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class PdfToTextViewModel
@Inject
constructor(private val toolsRepository: ToolsInterface,
            private val pdf2TextRepository: Pdf2TextInterface,
            private val isPdfLockedUseCase: IsPdfLockedUseCase,
            private val uiEventUseCase: UiEventUseCase,
            private val isPdfCorruptedUseCase: IsPdfCorruptedUseCase,
            private val getPdfThumbnailUseCase: GetPdfThumbnailUseCase,
            private val getPdfPageCountUseCase: GetPdfPageCountUseCase,
            private val getFileNameUseCase: GetFileNameUseCase,
            private val unlockPdfUseCase: UnlockPdfUseCase,
            private val convertToTextUseCase: ConvertToTextUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow(pdf2TextRepository.initPdfToTextUiState())
    val uiState: StateFlow<PdfToTextUiState> = _uiState.asStateFlow()

    val uiEventFlow = uiEventUseCase.uiEventFlow

    val allFilesInfo = pdf2TextRepository.getAllTextFiles().catch {
        emit(listOf())
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun setPdfText(text: String) {
        _uiState.update { state ->
            state.copy(text = text)
        }
    }



    private fun setTextFileName(text: String) {
        _uiState.update {
            it.copy(textFileName = text)
        }
    }
    /**
     * using pdf-box to generate thumbnail of pdf (Bitmap of first page of pdf using it's uri)
     */
    fun uploadPdf(uri:Uri){
        viewModelScope.launch {
            if(isPdfCorruptedUseCase(uri)){
                uiEventUseCase(EventType.Error)
            }
            else if (isPdfLockedUseCase(uri)) {
                _uiState.update { state ->
                    state.copy(uri = uri)
                }
                uiEventUseCase(EventType.ShowDialog)
            } else {
                pickPdf(uri)
            }
        }
    }
    fun unlockPdfAndUpload(uri:Uri,password: String){
        viewModelScope.launch {
            var newUri:Uri?=null
            var isSuccess=true
            try {
                newUri=unlockPdfUseCase(uri, password)
            }catch (e:Exception){
                isSuccess=false
                e.printStackTrace()
            }finally {
                if(isSuccess && newUri!=null){
                    pickPdf(newUri)
                }else{
                    uiEventUseCase(EventType.ShowDialogError)
                }
            }
        }
    }
    fun pickPdf(uri: Uri) {
        var isSuccess = true
        var thumbnail= toolsRepository.getDefaultThumbnail()
        var numPages = 0
        var fileName = ""
        viewModelScope.launch {
            try {
                setLoading(true)
                thumbnail = getPdfThumbnailUseCase(uri)
                numPages = getPdfPageCountUseCase(uri)
                fileName = getFileNameUseCase(uri)
            } catch (e: Exception) {
                e.printStackTrace()
                isSuccess = false
                uiEventUseCase(EventType.Error)
            } finally {
                if (isSuccess) {
                    _uiState.update { state ->
                        state.copy(
                            thumbnail = thumbnail,
                            numPages = numPages,
                            pdfFileName = fileName,
                            uri = uri
                        )
                    }
                    uiEventUseCase(EventType.Success)
                }
                setLoading(false)
            }
        }
    }

    fun convertToText(uri: Uri,pdfFileName: String) {
        var isSuccess = true
        var text = ""
        var info = Pair(0L, "")
        viewModelScope.launch {
            try {
                setLoading(true)
                text = convertToTextUseCase(uri=uri)
                info = pdf2TextRepository.createTextFile(text, pdfFileName)
            } catch (e: Exception) {
                e.printStackTrace()
                isSuccess = false
                uiEventUseCase(EventType.Error)
            } finally {
                if (isSuccess) {
                    _uiState.update {
                        it.copy(
                            text = text,
                            fileUniqueId = info.first,
                            textFileName = info.second,
                        )
                    }
                    uiEventUseCase(EventType.Success)
                }
                setLoading(false)
            }
        }
    }

    fun deleteFile(id: Long) {
        viewModelScope.launch {
            try {
                setLoading(true)
                pdf2TextRepository.deleteTextFile(id)
            } catch (e: Exception) {
                e.printStackTrace()
                uiEventUseCase(EventType.Error)
            }finally{
                setLoading(false)
            }
        }
    }

    fun readTextFromFile(id: Long) {
        var text = ""
        var textFileName = ""

        viewModelScope.launch {
            var isSuccess = true
            try {
                setLoading(true)
                val fileDetails = pdf2TextRepository.getTextAndNameFromFile(id)
                text = fileDetails.first
                textFileName = fileDetails.second
            } catch (e: Exception) {
                e.printStackTrace()
                uiEventUseCase(EventType.Error)
                isSuccess = false
            } finally {
                if (isSuccess) {
                    _uiState.update {
                        it.copy(
                            text = text,
                            textFileName = textFileName,
                            fileUniqueId = id,
                        )
                    }
                    uiEventUseCase(EventType.Success)
                }
                setLoading(false)
            }
        }
    }

    fun modifyFileName(name: String) {
        viewModelScope.launch {
            var isSuccess = true
            try {
                pdf2TextRepository.modifyName(uiState.value.fileUniqueId, "$name.txt")
            } catch (e: Exception) {
                uiEventUseCase(EventType.Error)
                isSuccess=false
                e.printStackTrace()
            }finally {
                if(isSuccess){
                    setTextFileName(name)
                }
            }
        }
    }
}