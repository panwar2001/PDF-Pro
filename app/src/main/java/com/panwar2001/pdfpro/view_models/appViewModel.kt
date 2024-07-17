package com.panwar2001.pdfpro.view_models

import android.app.LocaleManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.PdfRow
import com.panwar2001.pdfpro.data.ToolsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/**
 * Data class that represents the current UI state
 */
data class AppUiState(
    val text:String="",
    val query:String="",
    val isAscending: Boolean=false,
    val sortOption:Int=R.string.sort_by_date,
    val pdfsList:List<PdfRow> = listOf(),
    val searchBarActive:Boolean= false,
    val isBottomSheetVisible:Boolean=false,
    val pdfUri:Uri=Uri.EMPTY,
    val pdfName:String="",
    val numPages:Int=0)


@HiltViewModel
class AppViewModel @Inject constructor(@ApplicationContext val context: Context,
                                        private val toolsRepository: ToolsRepository): ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
    private val mimeType = "application/pdf"

    val options= listOf(
        R.string.sort_by_date,
        R.string.sort_by_size,
        R.string.sort_by_name)
    val languages = listOf(
            R.array.english,
            R.array.French,
            R.array.Japanese,
            R.array.Russian,
            R.array.hindi)

    init {
        searchPdfs()
    }
    /**
     * Reset the order state
     */
    fun resetState() {
        _uiState.value = AppUiState()
    }
    @WorkerThread
     fun getCurrentLocale(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales.toLanguageTags()
        } else {
            AppCompatDelegate.getApplicationLocales().toLanguageTags()
        }
    }
    fun setSearchText(query: String){
        _uiState.update {
            it.copy(query=query)
        }
    }
    fun toggleSortOrder(){
        _uiState.update {
            it.copy(isAscending =!it.isAscending)
        }
    }
    private fun setPdfsList(pdfsList:List<PdfRow>){
        _uiState.update {
            it.copy(pdfsList = pdfsList)
        }
    }
    fun setSearchBarActive(active:Boolean){
        _uiState.update {
            it.copy(searchBarActive = active)
        }
    }
 fun setUri(uriId:Long){
     try {
         viewModelScope.launch {
             _uiState.update {
                 val baseUri = MediaStore.Files.getContentUri("external")
                 val uri = ContentUris.withAppendedId(baseUri, uriId)
                 it.copy(
                     pdfUri = uri,
                     numPages = toolsRepository.getNumPages(uri),
                     pdfName = toolsRepository.getPdfName(uri)
                 )
             }
         }
     }catch(e: Exception){
         e.printStackTrace()
     }
    }
    fun setBottomSheetVisible(visible:Boolean){
        _uiState.update {
            it.copy(isBottomSheetVisible = visible)
        }
    }
    fun setSortOption(sortOption:Int){
        _uiState.update {
            it.copy(sortOption = sortOption)
        }
    }

    /**
     * TODO("check for how to set locale for less than android 13 version")
     */
    @WorkerThread
     fun setLocale(localeTag: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales=  LocaleList(
                Locale.forLanguageTag(localeTag))
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(
                    localeTag
                )
            )
        }
    }
    fun searchPdfs(){
            viewModelScope.launch {
                setPdfsList(toolsRepository.searchPdfs(
                    sortingOrder = uiState.value.sortOption,
                    ascendingSort = uiState.value.isAscending,
                    mimeType = "application/pdf",
                    query = uiState.value.query)
                )
            }
    }

    fun sharePdfFile(id:Long,startActivity:(shareIntent:Intent)->Unit){
        val baseUri = MediaStore.Files.getContentUri("external")
        val fileUri= ContentUris.withAppendedId(baseUri, id)
        val shareIntent= Intent().apply {
            action= Intent.ACTION_SEND
            type= mimeType
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Intent.EXTRA_STREAM,fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(shareIntent)
    }
}