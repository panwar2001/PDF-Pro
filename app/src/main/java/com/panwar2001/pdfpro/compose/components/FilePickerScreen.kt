package com.panwar2001.pdfpro.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.data.Tool
import com.panwar2001.pdfpro.compose.AppBar


@Composable
fun FilePickerScreen(onNavigationIconClick:()->Unit,
                     onClick: () -> Unit,
                     tool:Tool,
                     snackBarHostState: SnackbarHostState= remember { SnackbarHostState() }
) {
    Scaffold(snackbarHost ={ SnackbarHost(hostState = snackBarHostState)},
        topBar = {
        AppBar(onNavigationIconClick =onNavigationIconClick ) //Appbar scope end
    }) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        )
        {
            ToolDescription(stringResource(id = tool.toolDescription))
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            )
            {
              Button(buttonText = stringResource(id = tool.buttonDescription), onClick = onClick)
            }
        }
    } //Scaffold scope end
}


@Composable
fun Button(buttonText:String,onClick:()->Unit){
    ElevatedButton(
        onClick = onClick,
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


