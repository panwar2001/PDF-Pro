package com.panwar2001.pdfpro.data

import android.app.DownloadManager
import android.app.LocaleManager
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.LocaleList
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.core.os.LocaleListCompat
import androidx.documentfile.provider.DocumentFile
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.PdfRow
import com.panwar2001.pdfpro.view_models.AppUiState
import com.panwar2001.pdfpro.view_models.Img2PdfUiState
import com.panwar2001.pdfpro.view_models.PdfToImagesUiState
import com.panwar2001.pdfpro.view_models.PdfToTextUiState
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

//https://developer.android.com/codelabs/basic-android-kotlin-compose-add-repository#3

 @Singleton
 class ToolsRepository @Inject
 constructor(@ApplicationContext private val context: Context): ToolsInterfaceRepository{
     private val _progress = MutableStateFlow(0f)
     override val progress: StateFlow<Float> get() = _progress

    override suspend fun saveFileToDownload(uri: Uri, context: Context) {
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
         val contentResolver = context.contentResolver
         val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")
         fileDescriptor?.use { return  PdfRenderer(it).pageCount}
         return 0
     }

     @WorkerThread
     override suspend fun getPdfName(uri: Uri): String {
         val returnCursor =
             context.contentResolver.query(uri, null, null, null, null)
         returnCursor.use {
             if (it != null) {
                 val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                 it.moveToFirst()
                 val fileName = it.getString(nameIndex)
                 it.close()
                 return fileName
             }
         }
         throw NullPointerException("Could not get pdf file name")
     }

     @WorkerThread
     override suspend fun convertToText(uri: Uri): String {
         return withContext(Dispatchers.IO) {
             context.contentResolver.openInputStream(uri).use {
                 PDDocument.load(it).use { doc ->
                      PDFTextStripper().getText(doc)
                 }
             }
         }
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

     @WorkerThread
     override suspend fun images2Pdf(imageList: List<ImageInfo>): Uri{
             val document = PdfDocument()
             val length = imageList.size
             _progress.value=0f
            for (i in 0..<length) {
                 val uri = imageList[i].uri
                 val bitmap = uri.toBitmap()
                 val pageInfo = PageInfo.Builder(bitmap.width, bitmap.height, i + 1).create()
                 val page = document.startPage(pageInfo)
                 val canvas = page.canvas
                 val paint = Paint()
                 paint.color = Color.WHITE
                 canvas.drawBitmap(bitmap, 0f, 0f, null)
                 document.finishPage(page)
                 _progress.value=i * 1.0f / length
             }
             val newPdfFile = File(context.filesDir, "file.pdf")
       return withContext(Dispatchers.IO) {
             FileOutputStream(newPdfFile).use {
                 document.writeTo(it)
                 document.close()

                  FileProvider.getUriForFile(context,
                          context.packageName + ".fileprovider",
                                  newPdfFile)
             }
         }
     }
     private fun Uri.toBitmap(): Bitmap {
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

     /**
      * Gets the image information- type and it's size from it's uri
      * DocumentFile is a wrapper over File and does query internally
      * on a list of [uri] to get it's data
      *
      *  @param uri of image
      */
     override fun getImageInfo(uri:Uri):ImageInfo{
         val docFile= DocumentFile.fromSingleUri(context,uri)
         return ImageInfo(uri = uri,
             type = docFile?.type?:"",
             size = docFile?.length()?.toMB() ?: 0f)
     }
     override suspend fun pdfToImages(uri: Uri): List<Bitmap> {
         val contentResolver = context.contentResolver
         val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")
         _progress.value=0f
         if(fileDescriptor==null){
             throw NullPointerException("File descriptor is null")
         }
         fileDescriptor.use { descriptor ->
             val pdfRenderer = PdfRenderer(descriptor)
             pdfRenderer.use { renderer ->
                 val imagesList = mutableListOf<Bitmap>()
                 for (pageNum in 0 until renderer.pageCount) {
                     val page = renderer.openPage(pageNum)
                     val bitmap = Bitmap.createBitmap(
                         page.width,
                         page.height,
                         Bitmap.Config.ARGB_8888
                     )
                     val canvas = Canvas(bitmap)
                     val paint =Paint().apply { color= Color.WHITE}
                     canvas.drawRect(0f, 0f, page.width.toFloat(), page.height.toFloat(), paint)

                     page.render(
                         bitmap,
                         null,
                         null,
                         PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                     )
                     imagesList.add(bitmap)
                     _progress.value=pageNum * 1.0f / renderer.pageCount
                     page.close()
                 }
                 return imagesList
             }
         }
     }

     override fun getDefaultThumbnail(): Bitmap {
         return Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888)
     }

     override fun getUriFromMediaId(id: Long): Uri {
         val baseUri = MediaStore.Files.getContentUri("external")
         return ContentUris.withAppendedId(baseUri, id)
     }

     override fun initPdfToImagesUiState(): PdfToImagesUiState {
         return PdfToImagesUiState(
              uri=Uri.EMPTY,
              isLoading=false,
              thumbnail=getDefaultThumbnail(),
              fileName="file.pdf",
              images= listOf(),
              numPages=0
         )
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

     override fun initImg2PdfUiState(): Img2PdfUiState {
         return  Img2PdfUiState(
              imageList = listOf(),
              isLoading = false,
              fileName  = "file",
              fileUri   = Uri.EMPTY,
              numPages   =0
         )
     }
 }