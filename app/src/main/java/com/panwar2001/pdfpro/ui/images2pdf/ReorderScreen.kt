package com.panwar2001.pdfpro.ui.images2pdf

import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.panwar2001.pdfpro.R

/**
 * @param imageList
 * Cannot pass data class to imageList (On Android you can only use types which can be stored inside the Bundle.)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReorderScreen(navigateUp:()->Unit,
                  imageList:List<Uri>,
                  onMove: (Int, Int) -> Unit){
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.reorder),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1)
            },
            navigationIcon = {
                IconButton(onClick = {navigateUp()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
    }) { innerPadding ->
        Column(modifier=Modifier.padding(innerPadding)) {
            DraggableGrid(items = imageList, onMove =onMove) { item, isDragging, index->
                val elevation by animateDpAsState(if (isDragging) 4.dp else 1.dp, label = "elevation")
                Image(uri = item,elevation=elevation,index=index)
            }
        }
    }
}


@Composable
fun Image(uri: Uri,
          elevation: Dp,
          index:Int){
    Box{
        Badge(
            containerColor = Color.Red,
            contentColor = Color.White,
            modifier = Modifier
                .zIndex(20f)
                .align(Alignment.TopEnd)
        ) {
            Text("$index")
        }

        AsyncImage(model = uri,
            contentDescription ="",
            modifier= Modifier
                .shadow(elevation = elevation)
                .fillMaxWidth()
                .size(height = 160.dp, width = 160.dp)
                .padding(5.dp),
            contentScale = ContentScale.Crop)
    }
}
