package com.panwar2001.pdfpro.data

import android.app.LocaleManager
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toFile
import androidx.core.os.LocaleListCompat
import androidx.documentfile.provider.DocumentFile
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.PdfRow
import com.panwar2001.pdfpro.core.domain.GetFileSizeUseCase
import com.panwar2001.pdfpro.view_models.AppUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


interface ToolsInterface {
    val progress: StateFlow<Float>

    suspend fun searchPdfs(sortingOrder: Int, ascendingSort: Boolean, mimeType: String,query: String): List<PdfRow>

    suspend fun getThumbnailOfPdf(uri:Uri): Bitmap

    suspend fun getNumPages(uri:Uri):Int

    suspend fun getPdfName(uri: Uri): String

    fun getAppLocale(): String

    suspend fun setAppLocale(localeTag: String)

    fun getImageInfo(uri:Uri,isDocScanUri:Boolean=false):ImageInfo

    fun getDefaultThumbnail(): Bitmap

    fun getUriFromMediaId(id: Long): Uri

    fun initAppUiState(): AppUiState
}

//https://developer.android.com/codelabs/basic-android-kotlin-compose-add-repository#3

 @Singleton
 class ToolsRepository @Inject
 constructor(
     @ApplicationContext private val context: Context,
     private val getFileSizeUseCase: com.panwar2001.pdfpro.core.domain.GetFileSizeUseCase
     ): ToolsInterface{
     private val _progress = MutableStateFlow(0f)
     override val progress: StateFlow<Float> get() = _progress

     override suspend fun searchPdfs(sortingOrder: Int,
                                     ascendingSort: Boolean,
                                     mimeType: String,
                                     query:String): List<PdfRow>{

         val projection = arrayOf(
             MediaStore.Files.FileColumns._ID,
             MediaStore.Files.FileColumns.DATE_MODIFIED,
             MediaStore.Files.FileColumns.DISPLAY_NAME,
             MediaStore.Files.FileColumns.SIZE
         )

         val sortBy=when(sortingOrder){
             R.string.sort_by_name -> MediaStore.Files.FileColumns.DISPLAY_NAME
             R.string.sort_by_size -> MediaStore.Files.FileColumns.SIZE
             else-> MediaStore.Files.FileColumns.DATE_MODIFIED
         }
         val sortOrder= if(ascendingSort) "ASC" else "DESC"
         val whereClause = "${MediaStore.Files.FileColumns.MIME_TYPE} IN ('$mimeType') AND ${MediaStore.Files.FileColumns.DISPLAY_NAME} LIKE '%${query}%'"
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
                 return listPDF
             }
         }
         return listPDF
     }
     private fun Long.toTimeDateString(): String {
         val dateTime = Date(this*1000)
         val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
         return format.format(dateTime)
     }

     /**
      * convert Bytes into MegaBytes
      */
     private fun Long.toMB():Float{
         return  Math.round(this*100.0f/1048576)/100f
     }

     override  suspend  fun getThumbnailOfPdf(uri:Uri):Bitmap{
          return withContext(Dispatchers.IO) {
               val contentResolver = context.contentResolver
               val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")
               fileDescriptor.use { descriptor ->
                   if(descriptor==null){
                       throw NullPointerException("file descriptor is null")
                   }
                   PdfRenderer(descriptor).use { renderer ->
                       val page = renderer.openPage(0)
                       val bitmap = Bitmap.createBitmap(
                           page.width,
                           page.height,
                           Bitmap.Config.ARGB_8888
                       )
                       page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                       page.close()
                       bitmap
                   }
               }
           }
 }
    override suspend fun getNumPages(uri:Uri):Int {
         context.contentResolver.openFileDescriptor(uri, "r").use {
             if(it==null)return 0
             PdfRenderer(it).use { renderer->
                 return renderer.pageCount
             }
         }
     }

     @WorkerThread
     override suspend fun getPdfName(uri: Uri): String {
         context.contentResolver
             .query(uri, null, null, null, null)
             ?.use {
                 val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                 it.moveToFirst()
                 val fileName = it.getString(nameIndex)
                 it.close()
                 return fileName
         }
         throw NullPointerException("Could not get pdf file name")
     }

// TODO("check for how to set locale for less than android 13 version")
     override fun getAppLocale(): String {
         return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
             context.getSystemService(LocaleManager::class.java).applicationLocales.toLanguageTags()
         } else {
             AppCompatDelegate.getApplicationLocales().toLanguageTags()
         }
     }
//TODO("check for how to set locale for less than android 13 version")
     override suspend fun setAppLocale(localeTag: String) {
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

     /**
      * Gets the image information- type and it's size from it's uri
      * DocumentFile is a wrapper over File and does query internally
      * on a list of [uri] to get it's data
      *
      *  @param uri of image
      */
     override fun getImageInfo(uri:Uri,isDocScanUri:Boolean):ImageInfo{
         if(isDocScanUri){
             val file=uri.toFile()
             return ImageInfo(
                 uri = uri,
                 type = "image/jpeg",
                 size = getFileSizeUseCase( file.length())
             ) }
         else {
             val docFile = DocumentFile.fromSingleUri(context, uri)
             return ImageInfo(
                 uri = uri,
                 type = docFile?.type ?: "image/",
                 size = getFileSizeUseCase( docFile?.length())
             )
         }
     }

     override fun getDefaultThumbnail(): Bitmap {
         return Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888)
     }

     override fun getUriFromMediaId(id: Long): Uri {
         val baseUri = MediaStore.Files.getContentUri("external")
         return ContentUris.withAppendedId(baseUri, id)
     }

     override fun initAppUiState(): AppUiState {
         return AppUiState(
             text="",
             query="",
             isAscending=false,
             sortOption=R.string.sort_by_date,
             pdfsList = listOf(),
             searchBarActive= false,
             isBottomSheetVisible=false,
             pdfUri=Uri.EMPTY,
             pdfName="",
             numPages=0)
     }

 }