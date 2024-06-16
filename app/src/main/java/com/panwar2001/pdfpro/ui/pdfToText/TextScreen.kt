package com.panwar2001.pdfpro.ui.pdfToText

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.ui.AppBar

@Composable
fun TextScreen(text:String,onNavigationIconClick:()->Unit){
    val scrollState= rememberScrollState()
    Scaffold(topBar = {
            AppBar(onNavigationIconClick =onNavigationIconClick )
        }){padding->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(state = scrollState)
        ) {
                Text(
                    text = text,
                    modifier = Modifier.wrapContentSize().padding(5.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
        }
        }
}