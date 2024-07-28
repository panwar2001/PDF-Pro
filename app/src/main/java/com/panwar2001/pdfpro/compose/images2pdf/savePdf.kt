package com.panwar2001.pdfpro.compose.images2pdf

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.AppBar
import com.panwar2001.pdfpro.compose.components.RenameDialog


@Composable
fun SavePdfScreen(backNavigate:()->Unit,
                      navigateToPdfViewer: ()->Unit,
                      fileName:String,
                      fileUri:Uri,
                      uri:Uri,
                      renamePdfFile:(Uri,String)->Unit,
                      savePdfToExternalStorage:(externalStoragePdfUri:Uri,internalStoragePdfUri: Uri)->Unit) {
    val savePdf = rememberLauncherForActivityResult(
        contract= ActivityResultContracts.CreateDocument("application/pdf"),
        onResult = {
            if(it!=null) {
                savePdfToExternalStorage(it,fileUri)
            }
        }
    )
    var showDialog by remember { mutableStateOf(false) }
    RenameDialog(onDismissRequest = { showDialog=!showDialog}, fileName = fileName,
        modifyFileName ={
           renamePdfFile(fileUri,it)
        } , visible = showDialog)
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {Text(text = stringResource(id = R.string.save_to_storage),fontSize = 20.sp)},
                icon = { Icon(Icons.Filled.Create, null)},
                onClick = {savePdf.launch(fileName)},
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary)
        },
        topBar = {
            AppBar(onNavigationIconClick =backNavigate,
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                titleComposable = {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()){
                        Text(
                            text = "$fileName.pdf",
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { showDialog =!showDialog }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                modifier = Modifier
                                    .border(color = Color.LightGray, width = 4.dp)
                                    .padding(5.dp)
                            )
                        }
                    }
                })

        }) { innerPadding ->
        Column(  modifier = Modifier
            .padding(innerPadding)
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier.padding(100.dp),
                horizontalArrangement = Arrangement.Center
            ){
                val spacing= dimensionResource(id = R.dimen.spacing_large)
                Card(modifier= Modifier
                    .clickable { navigateToPdfViewer() },
                    colors= CardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.LightGray)
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(spacing)
                    ){
                        AsyncImage(model = uri,
                            contentDescription =null,
                            contentScale = ContentScale.Crop,
                            modifier= Modifier
                                .shadow(elevation = 5.dp)
                                .size(200.dp))
                        Spacer(modifier = Modifier.height(spacing))
                        Text(text = "$fileName.pdf",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewSavePdfScreen(){
    SavePdfScreen(
        backNavigate = { /*TODO*/ },
        navigateToPdfViewer = { /*TODO*/ },
        fileName = "file name",
        fileUri = Uri.EMPTY,
        uri = Uri.EMPTY,
        renamePdfFile ={ _, _-> },
        savePdfToExternalStorage = {_,_->}
    )
}