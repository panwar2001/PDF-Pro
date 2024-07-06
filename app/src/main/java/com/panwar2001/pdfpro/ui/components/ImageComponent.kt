package com.panwar2001.pdfpro.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.panwar2001.pdfpro.R

@Composable
fun ImageComponent(image: Any,
                   index:Int,
                   elevation:Dp){
    val openImageDialog = remember{ mutableStateOf(false) }
            AsyncImage(model = image,
                contentDescription =null,
                contentScale = ContentScale.Crop,
                modifier= Modifier
                    .shadow(elevation = elevation)
                    .size(50.dp)
                    .clickable { openImageDialog.value = !openImageDialog.value })

    if(openImageDialog.value){
    DialogWithImage(
        onDismissRequest = { openImageDialog.value = false },
        image = image,
        imageIndex = index + 1
    ) }
}

@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit,
    image: Any,
    imageIndex: Int,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 14.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ) {
                            Text("$imageIndex")
                        }
                    }
                ) {
                    AsyncImage(model = image,
                        contentDescription =null,
                        contentScale = ContentScale.Fit)

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = R.string.close))
                    }
                }
            }
        }
    }
}
