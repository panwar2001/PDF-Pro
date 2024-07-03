package com.panwar2001.pdfpro.ui.pdfToImages


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.ui.AppBar
import com.panwar2001.pdfpro.ui.components.ImageComponent


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
            LazyColumn(modifier= Modifier
                .padding(padding)){
                items(images.size) { index->
                    ImageComponent(images[index],
                             index+1,
                                   elevation = 0.dp)
                }
            }
    }
}
