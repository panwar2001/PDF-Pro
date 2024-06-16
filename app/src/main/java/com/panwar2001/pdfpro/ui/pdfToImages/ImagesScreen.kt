package com.panwar2001.pdfpro.ui.pdfToImages


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.panwar2001.pdfpro.ui.AppBar
import com.panwar2001.pdfpro.R


fun saveImageToStorage(uri: Uri,context:Context) {
    val contentResolver = context.contentResolver

    try {
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        val fileName = "my_image.jpg" // Replace with desired filename and extension
        val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // Adjust compression as needed
        outputStream.flush()
        outputStream.close()
        // Show success message or perform other actions
    } catch (e: Exception) {
        // Handle errors during saving
        e.printStackTrace()
    }
}
@Composable
fun ImagesScreen(images:List<ImageBitmap>,onNavigationIconClick:()->Unit){
    Scaffold(topBar = {
        AppBar(onNavigationIconClick =onNavigationIconClick )
    }){padding->
        Column {
            LazyColumn(modifier= Modifier
                .padding(padding)){
                items(images.size) { index->
                    ImageRow(images[index],index)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageRow(image:ImageBitmap,index:Int){
    val openImageDialog = remember{ mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 14.dp)
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = true,
            onCheckedChange = {  }
        )
        BadgedBox(
            badge = {
                    Badge(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ) {
                        Text("${index+1}")
                    }
            }
        ) {
            Image(
                bitmap = image,
                contentDescription = stringResource(id = R.string.pdf2img),
                modifier= Modifier
                    .size(100.dp)
                    .border(
                        border = BorderStroke(1.dp, Color.Black)
                    )
                    .clickable {
                        openImageDialog.value = !openImageDialog.value
                    }
            )
        }
    }
    if(openImageDialog.value){
        DialogWithImage(
            onDismissRequest = { openImageDialog.value=false },
            image = image,
            imageIndex =  index+1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit,
    image: ImageBitmap,
    imageIndex: Int,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 14.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ) {
                            Text("$imageIndex")
                        }
                    }
                ) {
                    Image(
                        bitmap = image,
                        contentDescription = "$imageIndex",
                        contentScale = ContentScale.Fit,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = R.string.close))
                    }
                }
            }
        }
    }
}
