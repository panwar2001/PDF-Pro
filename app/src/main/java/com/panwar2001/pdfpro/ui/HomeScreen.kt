package com.panwar2001.pdfpro.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.ToolsData
import com.panwar2001.pdfpro.ui.components.SearchView

@Composable
fun HomeScreen(onNavigationIconClick:()->Unit,
    navigateTo: (String)->Unit) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Scaffold(
        topBar = {
            SearchView(
                state = textState,
                placeHolder = "Search here...",
                modifier = Modifier,
                onNavigationIconClick = onNavigationIconClick
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        )
        {
            Column(Modifier.fillMaxSize()) {
                Screen(
                    searchedText = textState.value.text,
                    list = DataSource.FeatureList,
                    navigateTo
                )
            }
        }

    }
}

/**
 *  Composable that iterates through various tools and display them on a card
 *
 *  @param searchedText the tool  searched via SearchView by the user
 *  @param list the list of tools which match with the [searchedText]
 */
@Composable
fun Screen(searchedText:String, list: List<ToolsData>,navigateTo: (String)->Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(20.dp)
    ) {
        items(items = list.filter {
            it.description.contains(searchedText, ignoreCase = true)
        }, key = {it.key}) {item ->
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
fun Card(item: ToolsData, modifier:Modifier=Modifier,navigateTo: (String)->Unit){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = Modifier
            .size(width = 10.dp, height = 130.dp)
            .padding(10.dp)
            .clickable { navigateTo(item.screen) }
        ,colors = CardDefaults.cardColors(
            containerColor =Color.White,
        )
    ) {
        Text(
            text = item.description,
            textAlign = TextAlign.Center, // make text center horizontal
            modifier = modifier
                .width(150.dp)
                .height(80.dp)
                .padding(5.dp)
                .wrapContentHeight(),// make text center vertical
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Icon(
            painter = painterResource(id = item.iconId),
            contentDescription = "text icon",
            modifier= modifier
                .size(width = 40.dp, height = 40.dp)
                .padding(vertical = 1.dp)
                .align(Alignment.CenterHorizontally),
            tint=Color.Blue
        )
    }
}

@Preview
@Composable
fun Preview(){
    HomeScreen({}) {

    }
}

