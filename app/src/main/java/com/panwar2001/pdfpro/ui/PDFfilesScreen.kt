package com.panwar2001.pdfpro.ui
import android.content.ContentUris
import com.panwar2001.pdfpro.R
import android.content.Context
import android.database.Cursor
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.ui.components.sharePdfFile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@WorkerThread
@Composable
fun PdfFilesScreen(navigateTo:(String)->Unit,
                   sortFile:String,
                   ascendingOrder:Boolean,
                   searchedFileName:String) {
    val sortOption=when(sortFile){
        stringResource(id = R.string.sort_by_name)-> MediaStore.Files.FileColumns.DISPLAY_NAME
        stringResource(id = R.string.sort_by_size)-> MediaStore.Files.FileColumns.SIZE
        else-> MediaStore.Files.FileColumns.DATE_MODIFIED
    }
    val sortOrder=if(ascendingOrder) "ASC" else "DESC"
    val context = LocalContext.current
    Environment.getRootDirectory()
    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DATE_MODIFIED,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.SIZE
    )

    val mimeType = "application/pdf"
    val whereClause = "${MediaStore.Files.FileColumns.MIME_TYPE} IN ('$mimeType') AND ${MediaStore.Files.FileColumns.DISPLAY_NAME} LIKE '%$searchedFileName%'"
    val orderBy = "$sortOption $sortOrder"

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
                            navigateTo = navigateTo,
                            context=context)
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
        navigateTo: (String) -> Unit,
        context: Context) {
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
        Row(Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(id = R.drawable.pdf_svg),
                contentDescription = null,
                modifier = Modifier.size(50.dp))
            Column {
                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        text = name,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier=Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Share,
                        modifier = Modifier.clickable {
                            val baseUri = MediaStore.Files.getContentUri("external")
                            sharePdfFile(
                                context = context,
                                fileMimeType = "application/pdf",
                                fileURI = ContentUris.withAppendedId(baseUri, id)
                            )
                        },
                        contentDescription = null
                    )
                }
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
        navigateTo = {},
        context= LocalContext.current
    )
}

