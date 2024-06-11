package com.panwar2001.pdfpro.ui.pdfToImages


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.ui.AppBar

@Composable
fun ImagesScreen(images:List<ImageBitmap>,onNavigationIconClick:()->Unit){
    Scaffold(topBar = {
        AppBar(onNavigationIconClick =onNavigationIconClick )
    }){padding->
            LazyColumn(modifier=Modifier.padding(padding).fillMaxSize()){
                items(images.size) { index->
                    ImageRow(images[index])
                }
            }
    }
}
@Composable
fun ImageRow(image:ImageBitmap){
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = true,
            onCheckedChange = {  }
        )
        Image(
            bitmap = image,
            contentDescription = "image",
            modifier=Modifier.padding(5.dp)
        )
    }
}