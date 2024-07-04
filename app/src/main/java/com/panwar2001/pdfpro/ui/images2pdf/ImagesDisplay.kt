package com.panwar2001.pdfpro.ui.images2pdf


// https://stackoverflow.com/questions/76907718/compose-take-photo-with-camera-and-display-result-not-working

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Badge
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.ImageInfo
import com.panwar2001.pdfpro.ui.components.BottomIconButton
import com.panwar2001.pdfpro.ui.components.ImageComponent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesDisplay(navigateUp:()->Unit,
                  imageList:List<ImageInfo>,
                  navigateToReorder:()->Unit,
                  addImgUris:(List<Uri>)->Unit,
                  toggleCheckBox:(Int,Boolean)->Unit,
                  deleteImages:()->Unit,
                  convertToPdf:()->Unit){
    val imagesPickerLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia() ,
        onResult = {
            if(it.isNotEmpty()) {
                addImgUris(it)
            } })

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = convertToPdf,
                containerColor = Color.White,
                shape = CircleShape) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    modifier = Modifier.size(60.dp),
                    contentDescription = null,
                    tint = Color.Green,
                )
            }
        },
        topBar = {
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
            }
        )},
        bottomBar = {
            BottomAppBar (
                actions = {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()){

                        BottomIconButton(onToggle = deleteImages,
                            icon =  Icons.Default.Delete,
                            text =  stringResource(id = R.string.delete),
                            tint=Color.Unspecified)

                        BottomIconButton(onToggle = navigateToReorder,
                            icon =  R.drawable.arrow_sort,
                            text =  stringResource(id = R.string.reorder),
                            tint=Color.Unspecified)

                        BottomIconButton(onToggle = {
                            imagesPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )},
                            icon =  Icons.Outlined.AddCircle,
                            text =  stringResource(id = R.string.add_more),
                            tint=Color.Unspecified)

                        BottomIconButton(onToggle = {},
                            icon = R.drawable.camera,
                            text = stringResource(id = R.string.capture),
                            tint= LocalContentColor.current
                        )

                    }
                }
            )

        }) { innerPadding ->
            LazyColumn(modifier= Modifier.padding(innerPadding)) {
                items(imageList.size) { index->
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()){
                        Badge(
                            containerColor = Color.Red,
                            contentColor = Color.White,
                            modifier = Modifier
                                .zIndex(10f)) {
                            Text("${index+1}")
                        }
                        Checkbox(checked = imageList[index].checked,
                            onCheckedChange ={checked->toggleCheckBox(index,checked)},
                            modifier = Modifier
                                .zIndex(12f))

                        ImageComponent(imageList[index].uri,
                            index+1,
                            elevation = 0.dp)
                        Text(text = imageList[index].type)
                        Text(text = "${imageList[index].size} MB")
                    }
                    HorizontalDivider()
                }
            }
        }
    }
