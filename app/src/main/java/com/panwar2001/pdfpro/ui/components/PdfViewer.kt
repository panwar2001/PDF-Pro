package com.panwar2001.pdfpro.ui.components

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
fun PdfViewer( uri: Uri,
               navigateUp:()->Unit,
               fileName:String,
               numPages: Int) {
    var nightMode by remember {mutableStateOf(false)}
    var jumpToPage by remember { mutableIntStateOf(1) }
    var showDialog by remember { mutableStateOf(false) }
    var horizontal by remember { mutableStateOf(false) }
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
            })
    }, bottomBar = {
        BottomAppBar (
            actions = {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()){
                    BottomIconButton(onToggle = {nightMode=!nightMode},
                        icon = if(nightMode) R.drawable.light else R.drawable.night_moon ,
                        text = if(nightMode) "Light Mode" else "Dark Mode",
                        tint=Color.Unspecified)

                    BottomIconButton(onToggle = {horizontal=!horizontal},
                        icon = R.drawable.file,
                        text = if(horizontal) "vertical" else "horizontal",
                        tint= LocalContentColor.current)

                    BottomIconButton(onToggle = {showDialog=!showDialog},
                        icon = R.drawable.pagefile,
                        text = "Jump To",
                        tint= LocalContentColor.current)
                }
            }
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(horizontal) {
                PdfView(
                    uri = uri,
                    darkMode = nightMode,
                    pageNumber = jumpToPage,
                    horizontal = true
                )
            }else{
                PdfView(
                    uri = uri,
                    darkMode = nightMode,
                    pageNumber = jumpToPage,
                    horizontal = false
                )
            }
            if (showDialog) {
                PageNumberInputDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { number ->
                        showDialog = false
                        jumpToPage=number
                    }, numPages = numPages
                )
            }
        }
    }
}
@Composable
fun PdfView(uri: Uri,
            darkMode:Boolean,
            pageNumber:Int,
            horizontal:Boolean
            ){
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            PDFView(ctx, null).apply {
                fromUri(uri)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .swipeHorizontal(horizontal)
                    .nightMode(false)
                    .load()
            }
        },
        update = {
            it.setNightMode(darkMode)
            it.jumpTo(pageNumber)
            it.loadPages()
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageNumberInputDialog(onDismiss: () -> Unit,
                          onConfirm: (Int) -> Unit,
                          numPages:Int) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    AlertDialog(
        title = {
                Text(text = "Jump To Page",
                    fontWeight = FontWeight.Bold
                )
        },
        onDismissRequest = onDismiss,
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        isError = text.toIntOrNull() == null
                    },
                    placeholder = {
                        Text(text = "Page 1 to $numPages")
                    },
                    label = { Text("Page Number") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    isError = isError
                )
                if (isError) {
                    Text(
                        text = "Please enter a valid page number( 1 to $numPages ) ",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val number = text.toIntOrNull()
                    if (number != null && number<=numPages) {
                        onConfirm(number-1)
                    } else {
                        isError = true
                    }
                },
                colors=ButtonDefaults.buttonColors(containerColor = Color.Red,
                    contentColor = Color.White)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                   Text(text = "Cancel")
                }
        })
}

@Composable
fun BottomIconButton(onToggle:()->Unit,
                     icon:Int,
                     text:String,
                     tint:Color){
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 5.dp)){
        IconButton(onClick = onToggle) {
            Icon(
                painter = painterResource(id = icon ),
                modifier=Modifier.size(30.dp),
                contentDescription = text,
                tint = tint
            )
        }
        Text(text = text)
    }
}
@Preview
@Composable
fun Display() {
    PdfViewer(uri = Uri.EMPTY, navigateUp = { /*TODO*/ }, fileName ="dfk" , numPages =4 )
}