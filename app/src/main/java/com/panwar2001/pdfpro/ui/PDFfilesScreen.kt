package com.panwar2001.pdfpro.ui
import android.content.ContentUris
import com.panwar2001.pdfpro.R
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.data.Screens
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@WorkerThread
@Composable
fun PdfFilesScreen(navigateTo:(String)->Unit) {
    val context = LocalContext.current
    Environment.getRootDirectory()
    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DATE_MODIFIED,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.SIZE
    )

    val mimeType = "application/pdf"
    val whereClause = "${MediaStore.Files.FileColumns.MIME_TYPE} IN ('$mimeType')"
    val orderBy = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"

    val cursor: Cursor? = context.contentResolver.query(
        MediaStore.Files.getContentUri("external"),
        projection,
        whereClause,
        null,
        orderBy
    )

    cursor?.use {
        if (it.moveToFirst()) {
                val idCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val modifiedCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
                val nameCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val sizeCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)

            LazyColumn {
                do {
                    val dateModified = it.getLong(modifiedCol).toTimeDateString()
                    val name = it.getString(nameCol)
                    val size = it.getLong(sizeCol).toMB()
                    val id = cursor.getLong(idCol)

                   item{
                        PDF(
                            dateModified = dateModified,
                            name = name,
                            size = size,
                            id = id,
                            navigateTo = navigateTo)
                    }

            } while (it.moveToNext())
            }

        }
    }
}
fun Long.toTimeDateString(): String {
    val dateTime = Date(this*1000)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
    return format.format(dateTime)
}
fun Long.toMB():Float{
    return  Math.round(this*100.0f/1048576)/100f
}
@Composable
fun PDF(dateModified:String,
        size:Float,
        name: String,
        id:Long,
        navigateTo: (String) -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                       navigateTo("${Screens.PdfViewer.route}/${id}")
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Row(Modifier.padding(10.dp)){
            Image(painter = painterResource(id = R.drawable.pdf_svg),
                contentDescription = null,
                modifier = Modifier.size(50.dp))
            Column {
                Text(text = name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1)
                Row{
                    Text("$size MB")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text("$dateModified ")
                }
            }
        }
      }
 }
@Composable
@Preview
fun PreviewPdf(){
    PDF(dateModified = "18/03/2002",
        size = 34.0f,
        name ="file.pdf",
        id=1,
        navigateTo = {}
    )
}

fun generatePdfThumbnail(
    context: Context,
    pdfUri: Uri?,
    width: Int,
    height: Int
): Bitmap? {
    var fileDescriptor: ParcelFileDescriptor? = null
    var pdfRenderer: PdfRenderer? = null
    var page: PdfRenderer.Page? = null
    var bitmap: Bitmap? = null
    try {
        // Get the content resolver instance
        val contentResolver = context.contentResolver
        // Open the PDF file
        fileDescriptor = contentResolver.openFileDescriptor(pdfUri!!, "r")
        if (fileDescriptor != null) {
            // Create PdfRenderer instance
            pdfRenderer = PdfRenderer(fileDescriptor)

            // Open the specified page
            page = pdfRenderer.openPage(0)
            // Create a bitmap to render the page
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            // Render the page to the bitmap
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Close the page
            page.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        pdfRenderer?.close()
        if (fileDescriptor != null) {
            try {
                fileDescriptor.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    return bitmap
}