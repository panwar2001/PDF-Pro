package com.panwar2001.pdfpro.ui.PdfToText

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.ui.AppBar

@Composable
fun PreviewFileScreen(onNavigationIconClick:()->Unit,
                 navigateTo: (String)->Unit,
                  thumbnail:ImageBitmap?) {
    Scaffold(topBar = {
        AppBar(onNavigationIconClick =onNavigationIconClick ) //Appbar scope end
    }) { innerPadding ->
        Row(
            modifier = Modifier.fillMaxWidth().padding(innerPadding),
            horizontalArrangement = Arrangement.Center
        ){
            if(thumbnail!=null) {
                Image(
                    bitmap = thumbnail,
                    contentDescription = "jadu tona",
                    modifier = Modifier.size(300.dp)
                )
            }
        }
    } //Scaffold scope end
}