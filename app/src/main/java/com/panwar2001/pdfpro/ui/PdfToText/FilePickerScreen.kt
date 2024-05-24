package com.panwar2001.pdfpro.ui.PdfToText

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.util.Log
import kotlin.concurrent.schedule
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.panwar2001.pdfpro.ui.components.ProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Timer
import kotlin.concurrent.timerTask


fun getToolData(id:Int): Tool{
    return when(id){
       0-> Tool("Our advanced text extraction algorithm ensures high accuracy, preserving formatting and layout faithfully.","Select PDF file")
       else-> Tool("","upload")
    }
}
@Composable
fun FilePickerScreen(onNavigationIconClick:()->Unit,
                     setUri: (Uri?)->Unit,
                     scope:CoroutineScope,
                     generateThumbnail:()->Unit,
                     navigateTo: (String)->Unit) {

                        Scaffold(topBar = {
                            AppBar(onNavigationIconClick =onNavigationIconClick ) //Appbar scope end
                        }) { innerPadding ->
                                UploadScreenContent(
                                    innerPadding = innerPadding,
                                    id = 0,
                                    navigateTo = navigateTo,
                                    setUri,
                                    scope,
                                    generateThumbnail
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
                        scope: CoroutineScope,
                        generateThumbnail:()->Unit){
    var loading by remember { mutableStateOf(false) }
    val resultLauncher= rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == -1) { // -1 constant represents operation succeeded
                setUri(it.data?.data)
        }
    }
    val pdfIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"
    }
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
                        loading=true
                        resultLauncher.launch(pdfIntent)
                        CoroutineScope(Dispatchers.Default).launch {
                            // Simulate loading data
                            // Update data on the main thread
                            delay(4000)
                            withContext(Dispatchers.Main) {
                                generateThumbnail()
                                navigateTo(Screens.PdfToText.previewFile.route)
                            }
                        }
                        },
                    enabled = !loading,
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
                if (loading) {
                    ProgressIndicator(modifier = Modifier)
                }
            }

        }
    }




