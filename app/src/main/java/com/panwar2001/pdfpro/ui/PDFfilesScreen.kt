package com.panwar2001.pdfpro.ui
import android.content.ContentUris
import com.panwar2001.pdfpro.R
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.ui.components.sharePdfFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
data class PdfRow(
    val dateModified: String,
    val name: String,
    val size: Float,
    val id: Long
)
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
          }
        }
    PdfsList(listPDF = listPDF,
             context=context,
             navigateTo = navigateTo)
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
fun PdfsList(listPDF:MutableList<PdfRow>,
             context: Context,
             navigateTo: (String) -> Unit) {
    val scope = rememberCoroutineScope()
    Box {
        val listState = rememberLazyListState()
        LazyColumn(state = listState) {
            items(listPDF) { pdfItem ->
                PDF(
                    dateModified = pdfItem.dateModified,
                    name = pdfItem.name,
                    size = pdfItem.size,
                    id = pdfItem.id,
                    navigateTo = navigateTo,
                    context = context
                )
            }
        }
        // Show the button if the first visible item is past
        // the first item. We use a remembered derived state to
        // minimize unnecessary compositions
        val showButton by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }
        AnimatedVisibility(visible = showButton,
                           modifier=Modifier.padding(10.dp)) {
            ScrollToTopButton(onClick={
                scope.launch {
                    // Animate scroll to the first item
                    listState.animateScrollToItem(index = 0)
                }
            })
        }
    }
}
@Composable
fun ScrollToTopButton(onClick:()->Unit){
    IconButton(
        onClick = onClick,
        modifier = Modifier.border(width = 1.dp, color = Color.Black, shape = CircleShape).size(50.dp),
        colors = IconButtonDefaults.iconButtonColors(Color.Transparent)
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .rotate(-90f),
            tint = Color.Black
        )
    }
}
@Composable
@Preview
fun PreviewScrollButton(){
    ScrollToTopButton {

    }
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

