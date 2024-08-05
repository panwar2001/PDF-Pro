package com.panwar2001.pdfpro.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.AppBar
import com.panwar2001.pdfpro.compose.MenuItem
import com.panwar2001.pdfpro.data.Tool


@Composable
fun FilePickerScreen(onNavigationIconClick:()->Unit,
                     onClick: () -> Unit,
                     tool:Tool,
                     menuItems: List<MenuItem> = listOf(),
                     isLoading:Boolean=false,
                     snackBarHostState: SnackbarHostState,
) {
    Scaffold(snackbarHost ={SnackBarHost(snackBarHostState)},
        topBar = {
        AppBar(onNavigationIconClick =onNavigationIconClick,
            menuItems = menuItems,
            titleComposable = {
                Text(text = stringResource(id = R.string.pdf_file_picker))
            }) //Appbar scope end
    }) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        )
        {
            AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            ToolDescription(stringResource(id = tool.toolDescription))
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            )
            {
                Button(buttonText = stringResource(id = tool.buttonDescription),
                  onClick = onClick,
                  enabled = !isLoading)
            }
        }
    } //Scaffold scope end
}


@Composable
fun Button(buttonText:String,onClick:()->Unit,enabled: Boolean){
    ElevatedButton(
        onClick = onClick,
        enabled=enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(2.dp, color = Color.Black),
        modifier = Modifier.padding(vertical = 60.dp)
    ) {
        Text(
            text = buttonText,
            fontSize = 25.sp,
            modifier = Modifier.padding(4.dp)
        )
    }
}
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


@Composable
@Preview
fun PrevFilePickerScreen(
    snackBarHostState: SnackbarHostState= remember { SnackbarHostState() }
){
    var loading by remember {
        mutableStateOf(false)
    }
    FilePickerScreen(onNavigationIconClick = { /*TODO*/ },
        onClick = { loading=!loading},
        tool = Tool(R.string.img2pdf_description,R.string.sort_by_size),
        isLoading = loading,
        snackBarHostState = snackBarHostState)
}
