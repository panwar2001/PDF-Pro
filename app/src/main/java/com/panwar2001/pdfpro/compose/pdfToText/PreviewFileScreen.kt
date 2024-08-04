package com.panwar2001.pdfpro.compose.pdfToText

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.AppBar
import com.panwar2001.pdfpro.compose.MenuItem
import com.panwar2001.pdfpro.compose.components.SnackBarHost
import com.panwar2001.pdfpro.data.Tool

@Composable
fun PreviewFileScreen(onNavigationIconClick:()->Unit,
                      thumbnail:Bitmap,
                      fileName:String,
                      convertToText:()->Unit,
                      tool: Tool,
                      navigateToPdfViewer:()->Unit,
                      menuItems: List<MenuItem> = listOf(),
                      isLoading: Boolean,
                      snackBarHostState: SnackbarHostState= remember {SnackbarHostState()}
) {

    Scaffold(
        snackbarHost = {SnackBarHost(snackBarHostState)},
        floatingActionButton = {
            FloatingActionButton(onClick = {convertToText()},
                containerColor = Color.Red,
                contentColor = Color.White,
                modifier = Modifier.clickable(enabled = !isLoading){
                }) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)
                ){
                    Text(text = stringResource(id =  R.string.convert_to_text),
                        fontSize = 20.sp)
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                         stringResource(id = R.string.next))
                }
            }
        },
        topBar = {
        AppBar(onNavigationIconClick =onNavigationIconClick,
               menuItems = menuItems) //Appbar scope end
    }) { innerPadding ->
        Column(  modifier = Modifier
            .padding(innerPadding)
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            ToolDescription(description = stringResource(id = tool.toolDescription))
            Row(
                modifier = Modifier.padding(100.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Box(modifier= Modifier
                    .background(color = Color.LightGray)
                    .clickable {
                        navigateToPdfViewer()
                    }){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        AsyncImage(model = thumbnail,
                            contentDescription = stringResource(id = R.string.pdf2text),
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
} //Scaffold scope end

@Composable
fun ToolDescription(description:String){
    Text(
        text = description,
        modifier = Modifier.padding(
            start = 20.dp,
            end = 20.dp,
            top = 30.dp
        ),
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.Monospace
    )
}
