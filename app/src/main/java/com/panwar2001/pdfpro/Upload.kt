package com.panwar2001.pdfpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.panwar2001.pdfpro.ui.theme.PDFProTheme
import kotlinx.coroutines.launch

data class Tool(
    val toolDescription:String,
    val buttonDescription:String
)
fun getToolData(id:Int):Tool{
    return when(id){
       0-> Tool("Our advanced text extraction engine ensures high accuracy, preserving formatting and layout faithfully.","Upload pdf")
       else-> Tool("","upload")
    }
}
class Upload : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print(intent.getIntExtra("ID",0))
        val tool=getToolData(intent.getIntExtra("ID",0))
        setContent {
            PDFProTheme {
                val scaffoldState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                       Scaffold(topBar = {
                           AppBar(onNavigationIconClick = { scope.launch {
                             scaffoldState.apply {
                                 if(isClosed) open() else close()
                              }
                            }}) //Appbar ends
                       }) {
                           innerPadding->
                           Column(modifier=Modifier.padding(innerPadding)){
                              Text(text =tool.toolDescription)
                           }
                           ElevatedButton(onClick = {  }) {
                               Text(tool.buttonDescription)
                           }
                       } //Scaffold end
                }
            }
        }
    }
}

