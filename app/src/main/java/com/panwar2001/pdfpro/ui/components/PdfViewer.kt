package com.panwar2001.pdfpro.ui.components

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import com.panwar2001.pdfpro.R

/**
 *  This composable Display's the pdf utilizing Android View and library
 *
 *  @param uri  pdf content uri
 *  @param navigateUp navigate back
 *  @param fileName  pdf file name with extension
 */
@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewer( uri: Uri,navigateUp:()->Unit,fileName:String) {
    var nightMode by remember {
        mutableStateOf(false)
    }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = fileName,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1)},
            navigationIcon = {
                IconButton(onClick = {navigateUp()}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }, actions = {
                Column {
                IconButton(onClick = {
                    nightMode=!nightMode
                }) {
                        Icon(
                            painter = painterResource(id = if(nightMode)R.drawable.light else R.drawable.night_moon),
                            contentDescription = "night Mode",
                        )
                    }
                    Text(text = if(nightMode) "Light Mode" else "Dark Mode")
                }
            })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(nightMode){
                PdfView(uri = uri, darkMode =true)
            }else{
                PdfView(uri = uri, darkMode =false)
            }
        }
    }
}
@Composable
fun PdfView(uri: Uri,darkMode:Boolean){
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            PDFView(ctx, null).apply {
                fromUri(uri)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .nightMode(darkMode)
                    .load()
            }
        }
    )
}