package com.panwar2001.pdfpro.ui.images2pdf

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.panwar2001.pdfpro.ui.AppBar
import com.panwar2001.pdfpro.R


@Composable
fun SavePdfScreen(backNavigate:()->Unit,
                      navigateToPdfViewer: ()->Unit,
                      fileName:String,
                      uri:Uri) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
            },
                containerColor = Color.Red,
                contentColor = Color.White,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)
                ){
                    Text(text = stringResource(id = R.string.convert_to_images),
                        fontSize = 20.sp)
                    Icon(Icons.Filled.ArrowForward,
                        stringResource(id = R.string.next))
                }
            }
        },
        topBar = {
            AppBar(onNavigationIconClick =backNavigate)
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
                Box(modifier= Modifier
                    .background(color = Color.LightGray)
                    .clickable {navigateToPdfViewer()}){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        AsyncImage(model = uri,
                            contentDescription =null,
                            contentScale = ContentScale.Crop,
                            modifier= Modifier
                                .shadow(elevation = 5.dp))

                        Text(text = fileName,
                            modifier=Modifier.wrapContentSize(),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


//val = rememberLauncherForActivityResult(
//    contract = ActivityResultContracts.PickMultipleVisualMedia() ,
//    onResult = {
//        if(it.isNotEmpty()) {
//            addImgUris(it)
//        } })

