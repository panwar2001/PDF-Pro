package com.panwar2001.pdfpro.ui.pdfToText

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.data.Tool
import com.panwar2001.pdfpro.ui.AppBar


fun getToolData(id:Int): Tool{
    return when(id){
       0-> Tool("Our advanced text extraction algorithm ensures high accuracy, preserving formatting and layout faithfully.","Select PDF file")
       else-> Tool("","upload")
    }
}
@Composable
fun FilePickerScreen(onNavigationIconClick:()->Unit,
                     setUri: (Uri?)->Unit,
                     setLoading:(Boolean)->Unit,
                     navigateTo: (String)->Unit) {

                        Scaffold(topBar = {
                            AppBar(onNavigationIconClick =onNavigationIconClick ) //Appbar scope end
                        }) { innerPadding ->
                                UploadScreenContent(
                                    innerPadding = innerPadding,
                                    id = 0,
                                    navigateTo = navigateTo,
                                    setUri,
                                    setLoading
                                )
                        } //Scaffold scope end
}

/**
 * Composable that displays tool description and button to upload file
 *
 * @param innerPadding  inner padding of content
 * @param id unique id for a tool
 */

@Composable
fun UploadScreenContent(innerPadding:PaddingValues,
                        id:Int,
                        navigateTo: (String) -> Unit,
                        setUri: (Uri?)->Unit,
                        setLoading: (Boolean) -> Unit){
    val resultLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = {
            setUri(it)
            navigateTo(Screens.PdfToText.previewFile.route)
        }
    )

    val tool = getToolData(id)
    Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        )
        {

            Text(
                text = tool.toolDescription,
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
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            )
            {

                ElevatedButton(
                    onClick = {
                        resultLauncher.launch(arrayOf("application/pdf"))
                        setLoading(true)
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(2.dp, color = Color.Black),
                    modifier = Modifier.padding(vertical = 60.dp)
                ) {
                    Text(
                        text = tool.buttonDescription,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

        }
    }




