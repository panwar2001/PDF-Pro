package com.panwar2001.pdfpro.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowForward
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.model.ToolsData
import com.panwar2001.pdfpro.core.ui.DataSource
import com.panwar2001.pdfpro.core.ui.components.BannerAd
import com.panwar2001.pdfpro.core.ui.components.CircularIcon

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigationIconClick:()->Unit,
               navigateTo: (String)->Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {HomeScreenTopAppBar(scrollBehavior,
            onNavigationIconClick = onNavigationIconClick)},
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
          ComposeTools(navigateTo=navigateTo)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopAppBar(scrollBehavior:TopAppBarScrollBehavior,
                        onNavigationIconClick :()->Unit){

        TopAppBar(title = { Text("PdfPro",
                              fontWeight = FontWeight.Bold,
                              fontSize = 30.sp)},
            navigationIcon = {
                LeadingIcon(onNavigationIconClick = onNavigationIconClick,
                    iconImgVec = Icons.Outlined.Menu)
            },
            scrollBehavior=scrollBehavior)
}

@Composable
fun LeadingIcon(onNavigationIconClick: () -> Unit,
                iconImgVec: ImageVector,
                iconDesc:String?=null,
                iconSize: Dp=32.dp){
    IconButton(onClick = onNavigationIconClick) {
        Icon(
            imageVector = iconImgVec,
            contentDescription = iconDesc,
            modifier=Modifier.size(iconSize)
        )
    }
}
/**
 *  Composable that iterates through various tools and display them on a card
 *
 */
@Composable
fun ComposeTools(navigateTo: (String)->Unit) {
    LazyColumn(contentPadding = PaddingValues(20.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            BannerAd(adUnitResID = R.string.home_banner)
        }
        items(items = DataSource.ToolsList,
            key = {it.title}) {item ->
            Card(item =item,navigateTo=navigateTo)
        }
    }
}


/**
 * Composable of clickable card which displays tool information to user
 *
 * @param item contains data of a tool
 */
@Composable
fun Card(
    item: ToolsData,
    modifier:Modifier=Modifier,
    navigateTo: (String)->Unit
){
    val desId= DataSource.getToolData(item.title).toolDescription
    androidx.compose.material3.Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = modifier
            .clickable(indication = rememberRipple(),
                interactionSource = remember {
                    MutableInteractionSource()
                },
                onClick = { navigateTo(item.route) })
            .fillMaxWidth()
            .padding(dimensionResource(id = com.panwar2001.pdfpro.core.ui.R.dimen.spacing_large))
        ,colors = CardDefaults.cardColors(
            containerColor = if(MaterialTheme.colorScheme.background.luminance() > 0.5)Color.White else Color.Unspecified,
        )
    ) {
        Column(   modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(18.dp)
            )) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularIcon(
                    iconResourceId = item.iconId,
                    backgroundColor = Color(item.iconColor),
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = stringResource(id = item.title),
                    textAlign = TextAlign.Center, // make text center horizontal
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                )
                Spacer(modifier = Modifier.width(80.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 5.dp),
            ) {
                Text(
                    text = stringResource(id = desId),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(dimensionResource(id = com.panwar2001.pdfpro.core.ui.R.dimen.spacing_small))
                        .weight(1f),
                    maxLines = 5,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Sharp.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        }
    }
@Preview
@Composable
fun Preview(){
    HomeScreen(
        onNavigationIconClick = { },
        navigateTo = {}
    )

}

