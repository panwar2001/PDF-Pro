package com.panwar2001.pdfpro.feature.pdf2img

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.panwar2001.pdfpro.compose.AppBar
import com.panwar2001.pdfpro.data.Tool
import com.panwar2001.pdfpro.screens.Screens

@Composable
fun PreviewFileScreen(onNavigationIconClick:()->Unit,
                      navigateTo: (String)->Unit,
                      thumbnail: Bitmap,
                      fileName:String,
                      setLoading:(Boolean)->Unit,
                      convertToImages:()->Unit,
                      tool: Tool) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
               setLoading(true)
               convertToImages()
                navigateTo(com.panwar2001.pdfpro.screens.Screens.PdfToImage.ImageScreen.route)
            },
                containerColor = Color.Red,
                contentColor = Color.White,
                ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)
                ){
                    Text(text = stringResource(id = R.string.convert_to_images),
                        fontSize = 20.sp)
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                         stringResource(id = R.string.next))
                }
            }
        },
        topBar = {
        AppBar(onNavigationIconClick =onNavigationIconClick ) //Appbar scope end
    }) { innerPadding ->
        Column(  modifier = Modifier
            .padding(innerPadding)
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(id = tool.toolDescription),
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 30.dp
                ),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace
            )
            Row(
                modifier = Modifier.padding(100.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Box(modifier= Modifier
                    .background(color = Color.LightGray)
                    .clickable {
                        navigateTo(com.panwar2001.pdfpro.screens.Screens.PdfToImage.PdfViewer.route)
                    }){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        AsyncImage(model = thumbnail,
                            contentDescription = stringResource(id = R.string.pdf2img),
                            modifier=Modifier.padding(5.dp))

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


