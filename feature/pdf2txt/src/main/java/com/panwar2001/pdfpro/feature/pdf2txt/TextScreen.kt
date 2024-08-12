package com.panwar2001.pdfpro.feature.pdf2txt

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.compose.AppBar
import com.panwar2001.pdfpro.ui.components.RenameDialog

@Composable
fun TextScreen(text:String,
               onNavigationIconClick:()->Unit,
               onBackPress:()->Unit={},
               fileName: String,
               modifyFileName:(String)->Unit,
               scrollState: ScrollState=rememberScrollState()){
    var showDialog by remember { mutableStateOf(false) }
    BackHandler {
        onBackPress()
    }
    Scaffold(topBar = {
            AppBar(onNavigationIconClick =onNavigationIconClick,
                  titleComposable = {
                      Row(verticalAlignment = Alignment.CenterVertically,
                          horizontalArrangement = Arrangement.SpaceBetween,
                          modifier = Modifier.fillMaxWidth()){
                          Text(
                              text = "$fileName.txt",
                              overflow = TextOverflow.Ellipsis,
                              maxLines = 1,
                              modifier = Modifier.weight(1f)
                          )
                          IconButton(onClick = { showDialog =!showDialog }) {
                              Icon(
                                  imageVector = Icons.Outlined.Edit,
                                  contentDescription = null,
                                  modifier = Modifier.border(color = Color.LightGray, width = 4.dp).padding(5.dp)
                              )
                          }
                      }
                  })
        }){padding->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(state = scrollState)
        ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(5.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
        }
        com.panwar2001.pdfpro.ui.components.RenameDialog(
            onDismissRequest = { showDialog = false },
            fileName = fileName,
            modifyFileName = modifyFileName,
            visible = showDialog
        )
    }
}



@Composable
@Preview
fun PreviewTextScreen(){
TextScreen(text = "",
    onNavigationIconClick = { /*TODO*/ },
    fileName ="file name is very large in length.txt" ,
    modifyFileName = {},
)
}