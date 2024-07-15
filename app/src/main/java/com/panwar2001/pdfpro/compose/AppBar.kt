package com.panwar2001.pdfpro.compose

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar( onNavigationIconClick: () -> Unit){
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = {onNavigationIconClick() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Navigation Bar Icon",
                    modifier= Modifier.size(50.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* */}) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More options",
                    modifier= Modifier.size(50.dp),
                )
            }
        })

}

@Preview
@Composable
fun AppBarPreview(){
    AppBar(onNavigationIconClick = {})
}