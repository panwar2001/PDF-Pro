package com.panwar2001.pdfpro

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableSectionTitle(modifier: Modifier = Modifier, isExpanded: Boolean, title: String) {

    val icon = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown

    Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(32.dp),
            imageVector = icon,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimaryContainer),
            contentDescription = null
        )
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }
}
@Composable
fun Check(list: List<String>,title: String) {
    var isExpanded by remember { mutableStateOf(false) }
    Column(  modifier = Modifier
        .clickable { isExpanded = !isExpanded }
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .fillMaxWidth()){

          ExpandableSectionTitle(isExpanded = isExpanded, title = title)
            AnimatedVisibility(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxWidth(),
                visible = isExpanded
            ) {
                LazyColumn(
                    Modifier
                        .padding(16.dp)
                        .animateContentSize()
                ) {
                    items(list) { listItem ->
                        Text(text = listItem, modifier = Modifier.padding(8.dp))
                    }
                }
            }
    }
}
@SuppressLint("RememberReturnType")
@Preview
@Composable
fun Checker(){
    val t= remember {
        mutableListOf<String>()
    }
    repeat(10){
        t.add("move")
    }
    Check(t, title = "hello")
}