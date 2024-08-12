package com.panwar2001.pdfpro.compose.pdfToText

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.ui.components.SnackBarHost
import com.panwar2001.pdfpro.data.TextFileInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFilesScreen(filesInfo:List<TextFileInfo>,
                    navigateBack:()->Unit,
                    deleteFile:(id: Long)->Unit,
                    share:(Uri)->Unit,
                    viewTextFile:(Long)->Unit,
                    isLoading:Boolean,
                    snackBarHostState: SnackbarHostState= remember {SnackbarHostState()},
){

    var openDeleteFileDialog by remember { mutableStateOf(false) }

    Scaffold(
            snackbarHost = { com.panwar2001.pdfpro.ui.components.SnackBarHost(snackBarHostState) },
            topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.text_files_log))},
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigation Bar Icon",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                })
        }){padding->
        LazyColumn(Modifier.padding(padding)){
            item {
                AnimatedVisibility(visible = isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            items(filesInfo) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_tiny)))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewTextFile(it.id)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.spacing_large))
                            .weight(1f)){
                        Text(text = it.fileName,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(text = it.fileSize,
                            color = Color.Gray)
                    }
                    IconButton(onClick = {share(it.uri)}) {
                        Icon(imageVector = Icons.Outlined.Share,
                            contentDescription = null)
                    }

                    IconButton(onClick = {openDeleteFileDialog=true}) {
                        Icon(imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            tint = Color.Red)
                    }
                }
                HorizontalDivider()
                if(openDeleteFileDialog){
                    DeleteTextFileDialog(
                        onDismissRequest = { openDeleteFileDialog=false},
                        onConfirmation = { deleteFile(it.id)
                                         openDeleteFileDialog=false},
                        dialogTitle = R.string.delete_file_dialog_title,
                        dialogText = R.string.delete_file_dialog_text,
                        fileName = it.fileName
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteTextFileDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    @StringRes dialogTitle: Int,
    @StringRes dialogText: Int,
    fileName: String
) {
    AlertDialog(
        title = {Text(text = stringResource(id = dialogTitle))},
        text = {Text(text = stringResource(id = dialogText,fileName))},
        onDismissRequest = {onDismissRequest()},
        confirmButton = {
            TextButton(onClick = {onConfirmation()}) {Text(stringResource(id = R.string.confirm)) }
        },
        dismissButton = {
            TextButton(onClick = {onDismissRequest() }) {Text(stringResource(id = R.string.dismiss))}
        })
}


@Preview
@Composable
fun PreviewTextFilesScreen(){
    val filesInfo= mutableListOf<TextFileInfo>()
    filesInfo.add(TextFileInfo("file name  file name file name file name file name file name file name",3, Uri.EMPTY, fileSize = "0.4"))
    TextFilesScreen(filesInfo = filesInfo, navigateBack = { /*TODO*/ },
        viewTextFile = {}, share = {}, deleteFile = {},
        isLoading = true)
}