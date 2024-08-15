package com.panwar2001.pdfpro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularIcon(modifier: Modifier=Modifier,
                 backgroundColor:Color= Color.Blue,
                 iconResourceId:Int,
                 size:Dp=24.dp){
    CircularBox(modifier,size, backgroundColor ) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = null,
            modifier = Modifier.size(size),
            tint = Color.White
        )
    }
}

@Composable
fun CircularIcon(modifier: Modifier=Modifier,
                 backgroundColor:Color= Color.Blue,
                 icon: ImageVector,
                 size:Dp=24.dp){
    CircularBox(modifier,size, backgroundColor ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier= Modifier.size(size),
            tint = Color.White
        )
    }
}

@Composable
fun CircularBox(
    modifier: Modifier=Modifier,
    size: Dp,
    backgroundColor: Color,
    content: @Composable ()->Unit
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clip(CircleShape)
            .size(size * 1.7f)
            .background(color = backgroundColor),
        contentAlignment = Alignment.Center,
    ){
        content()
    }
}