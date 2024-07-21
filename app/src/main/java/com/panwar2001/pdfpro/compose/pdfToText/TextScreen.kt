package com.panwar2001.pdfpro.compose.pdfToText

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.AppBar

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
                              text = fileName,
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
        if(showDialog){
            TextInputDialog(onDismissRequest = { showDialog=false},
                fileName = fileName,
                modifyFileName=modifyFileName)
        }
    }
}

@Composable
fun TextInputDialog(onDismissRequest: () -> Unit,
                    fileName: String,
                    modifyFileName: (String) -> Unit){
    var text by remember { mutableStateOf(fileName) }
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(modifier = Modifier
            .fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius))
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier.padding(dimensionResource(id = R.dimen.spacing_large))){
                OutlinedTextField(
                    value = text,
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                    },
                    onValueChange = {
                        text = it
                        isError = text.toIntOrNull() == null
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    isError = isError,
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large)))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Dismiss")
                    }
                    TextButton(onClick = { modifyFileName(text)
                    onDismissRequest()}) {
                        Text(text = "Save")
                    }
                }
            }
        }
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