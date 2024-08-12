package com.panwar2001.pdfpro.feature.pdf2img


import android.graphics.Bitmap
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.compose.AppBar
import com.panwar2001.pdfpro.ui.components.ImageComponent

@Composable
fun ImagesScreen(images:List<Bitmap>,onNavigationIconClick:()->Unit){
    Scaffold(topBar = {
        AppBar(onNavigationIconClick =onNavigationIconClick )
    }){padding->
            LazyColumn(modifier= Modifier
                .padding(padding)){
                items(images.size) { index->
                    com.panwar2001.pdfpro.ui.components.ImageComponent(
                        images[index],
                        index + 1,
                        elevation = 0.dp
                    )
                }
            }
    }
}
