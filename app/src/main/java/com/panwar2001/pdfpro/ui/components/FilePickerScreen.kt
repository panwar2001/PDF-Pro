package com.panwar2001.pdfpro.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.data.Tool
import com.panwar2001.pdfpro.ui.AppBar
import kotlinx.coroutines.launch


@Composable
fun FilePickerScreen(onNavigationIconClick:()->Unit,
                     setUri: (Uri)->Unit={},
                     setUris:(List<Uri>)->Unit={},
                     setLoading:(Boolean)->Unit,
                     navigate: ()->Unit,
                     selectMultipleFile: Boolean=false,
                     mimeType:String,
                     tool:Tool) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val tryAgain:()->Unit={
        scope.launch {
            snackBarHostState.showSnackbar(
                message = "Something went wrong. Please try again.",
                // Defaults to SnackbarDuration.Short
                duration = SnackbarDuration.Short
            )
        }
    }
    val resultLauncher= if(selectMultipleFile){
        rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments() ,
        onResult = {
            if(it.isNotEmpty()) {
                setUris(it)
                navigate()
            } })
    }else{
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(),
            onResult = {
                if(it!=null){
                        setUri(it)
                        navigate()
                }
            })

    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
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
            ToolDescription(tool.toolDescription)
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            )
            {
              Button(buttonText = tool.buttonDescription, onClick ={
                  try {
                      resultLauncher.launch(arrayOf(mimeType))
                      setLoading(true)
                  }
                  catch (e:Exception){
                      e.printStackTrace()
                  }
              })
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


