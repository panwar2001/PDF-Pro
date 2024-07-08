package com.panwar2001.pdfpro.ui.view_models

import android.app.DownloadManager
import android.app.LocaleManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.LocaleList
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.ui.PdfRow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
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
class AppViewModel @Inject constructor(@ApplicationContext val context: Context): ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val mimeType = "application/pdf"

    val options= listOf(
        R.string.sort_by_date,
        R.string.sort_by_size,
        R.string.sort_by_name)
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
     _uiState.update {
         val baseUri = MediaStore.Files.getContentUri("external")
         val uri = ContentUris.withAppendedId(baseUri, uriId)
         it.copy(pdfUri = uri, numPages = getNumPages(uri), pdfName = getPdfName(uri))
        }
    }
    @WorkerThread
    private fun getNumPages(uri:Uri): Int{
        val fileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
        fileDescriptor?.use { descriptor ->
            return PdfRenderer(descriptor).pageCount
        }
        return 0
    }
    private fun getPdfName(uri:Uri):String{
        //set file name
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        returnCursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.moveToFirst()
                return it.getString(nameIndex)
        }
        return ""
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
     * TODO:  check for how to set locale for less than android 13 version
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
    fun saveFileToDownload(uri:Uri,context: Context){
        val request = DownloadManager.Request(uri)
        request.setTitle("PDF Download") // Title of the download notification
        request.setDescription("Downloading PDF") // Description of the download notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        // Set destination in Downloads folder
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "example.pdf")

        // Enqueue the download and get download reference ID
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE)  as DownloadManager
        downloadManager.enqueue(request)
    }
    fun searchPdfs(){
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE
        )

        val sortBy=when(uiState.value.sortOption){
            R.string.sort_by_name -> MediaStore.Files.FileColumns.DISPLAY_NAME
            R.string.sort_by_size -> MediaStore.Files.FileColumns.SIZE
            else-> MediaStore.Files.FileColumns.DATE_MODIFIED
        }
        val sortOrder= if(uiState.value.isAscending) "ASC" else "DESC"
        val whereClause = "${MediaStore.Files.FileColumns.MIME_TYPE} IN ('$mimeType') AND ${MediaStore.Files.FileColumns.DISPLAY_NAME} LIKE '%${uiState.value.query}%'"
        val orderBy = "$sortBy $sortOrder"
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            whereClause,
            null,
            orderBy
        )
        val listPDF=mutableListOf<PdfRow>()

        cursor?.use {
            if (it.moveToFirst()) {
                val idCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val modifiedCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
                val nameCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val sizeCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                do {
                    val dateModified = it.getLong(modifiedCol).toTimeDateString()
                    val name = it.getString(nameCol)
                    val size = it.getLong(sizeCol).toMB()
                    val id = it.getLong(idCol)
                    listPDF.add(PdfRow(dateModified,name,size,id))
                } while (it.moveToNext())
                setPdfsList(listPDF)
            }
        }
    }
    private fun Long.toTimeDateString(): String {
        val dateTime = Date(this*1000)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
        return format.format(dateTime)
    }
    private fun Long.toMB():Float{
        return  Math.round(this*100.0f/1048576)/100f
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