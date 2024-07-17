package com.panwar2001.pdfpro.data

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.WorkerThread
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.PdfRow
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.schedule

//https://developer.android.com/codelabs/basic-android-kotlin-compose-add-repository#3

 @Singleton
 class ToolsRepository @Inject
 constructor(@ApplicationContext private val context: Context): ToolsInterfaceRepository{

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
     private fun Long.toMB():Float{
         return  Math.round(this*100.0f/1048576)/100f
     }

     @WorkerThread
     override  suspend  fun getThumbnailOfPdf(uri:Uri):ImageBitmap ?{
         val contentResolver = context.contentResolver
         val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")
         fileDescriptor?.use { descriptor ->
                 PdfRenderer(descriptor).use { renderer ->
                 val page = renderer.openPage(0)
                 val bitmap = Bitmap.createBitmap(
                     page.width,
                     page.height,
                     Bitmap.Config.ARGB_8888
                 )
                 page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                 page.close()
                 return bitmap.asImageBitmap()
             }
         }
         return null
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
         return ""
     }

     @WorkerThread
     override suspend fun convertToText(uri: Uri): String {
         context.contentResolver.openInputStream(uri).use {
                     PDDocument.load(it).use { doc->
                         return PDFTextStripper().getText(doc)
                     }
               }
         }
    }