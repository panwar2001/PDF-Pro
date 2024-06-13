package com.panwar2001.pdfpro.ui.images2pdf

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesDisplay(navigateUp:()->Unit,
                  imgUris:List<Uri>){
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Reorder",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1)
            },
            navigationIcon = {
                IconButton(onClick = {navigateUp()}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
    }) { innerPadding ->
        Column(modifier=Modifier.padding(innerPadding)) {
            LazyVerticalGrid(columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(items = imgUris) {uri->
                    AsyncImage(model = uri,
                        contentDescription ="",
                        modifier= Modifier
                            .fillMaxWidth()
                            .size(height = 160.dp, width = 160.dp)
                            .padding(5.dp),
                        contentScale = ContentScale.Crop)
                }
                item{
                    Card()
                }
            }
        }
    }
}
@Composable
fun Card(){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = Modifier
            .size(160.dp,160.dp)
            .padding(5.dp)
            .clickable {

            }
        ,colors = CardDefaults.cardColors(
            containerColor = Color.Gray,
        )
    ){
        Column(verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()){
            Icon(imageVector =Icons.Default.Add ,
                contentDescription = "Add images",
                modifier= Modifier
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally),
                tint=Color.White)

            Text(text = "Add Images",
                modifier=Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color=Color.White)
        }
    }
}