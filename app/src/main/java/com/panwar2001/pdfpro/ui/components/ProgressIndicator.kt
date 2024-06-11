package com.panwar2001.pdfpro.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressIndicator(modifier: Modifier){
    Column(modifier=modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) { CircularProgressIndicator() }
}

@Composable
fun DeterminateIndicator(progress:Float){
    Column(modifier=Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(100.dp)  // Adjust size as needed
        ) {
            CircularProgressIndicator(
                progress = progress,
                strokeWidth = 8.dp,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 20.sp
            )
        }
    }
}

@Preview
@Composable
fun Preview(){
    ProgressIndicator(Modifier)
}