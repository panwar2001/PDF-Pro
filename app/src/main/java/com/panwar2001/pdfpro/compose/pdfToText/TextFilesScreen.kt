package com.panwar2001.pdfpro.compose.pdfToText

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.TextFileInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFilesScreen(filesInfo:List<TextFileInfo>,
                    navigateBack:()->Unit,
                    deleteFile:(id: Long)->Unit){
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = "Text Files Log")},
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
            items(filesInfo) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_tiny)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = it.fileName,
                        Modifier.padding(horizontal = dimensionResource(id = R.dimen.spacing_large))
                            .weight(1f).wrapContentWidth(),
                    )
                    IconButton(onClick = {deleteFile(it.id)}) {
                        Icon(imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            tint = Color.Red)
                    }
                }
                HorizontalDivider()
            }
        }
    }
}

@Preview
@Composable
fun PreviewTextFilesScreen(){
    val filesInfo= mutableListOf<TextFileInfo>()
    filesInfo.add(TextFileInfo("file name  file name file name file name file name file name file name",3, Uri.EMPTY))
    TextFilesScreen(filesInfo = filesInfo, navigateBack = { /*TODO*/ }) {

    }
}