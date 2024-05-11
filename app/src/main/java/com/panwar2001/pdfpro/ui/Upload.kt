package com.panwar2001.pdfpro.ui

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.Tool
import com.panwar2001.pdfpro.ui.components.DrawerBody
import com.panwar2001.pdfpro.ui.components.DrawerHeader
import kotlinx.coroutines.launch


fun getToolData(id:Int): Tool{
    return when(id){
       0-> Tool("Our advanced text extraction algorithm ensures high accuracy, preserving formatting and layout faithfully.","Select PDF file")
       else-> Tool("","upload")
    }
}

@Composable
fun UploadScreen(navController: NavController) {

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(Modifier.padding(0.dp,0.dp,60.dp,0.dp)) {
                            DrawerHeader()
                            DrawerBody(
                                items = DataSource.MenuItems,
                                onItemClick = {
                                    navController.navigate(it.id)
                                }
                            )
                        }
                    })
                {
                        Scaffold(topBar = {
                            AppBar(onNavigationIconClick = {
                                scope.launch {
                             drawerState.apply {
                                 if(isClosed) open() else close()
                              }
                                }
                            }) //Appbar scope end
                        }) { innerPadding ->
                            UploadScreenContent(innerPadding = innerPadding,id=0)
                        } //Scaffold scope end
                }
}

/**
 * Composable that displays tool description and button to upload file
 *
 * @param innerPadding  inner padding of content
 * @param id unique id for a tool
 */
@Composable
fun UploadScreenContent(innerPadding:PaddingValues,id:Int){
        val tool = getToolData(id)
        Column(
            modifier = Modifier.padding(innerPadding)
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
                    onClick = { },
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




