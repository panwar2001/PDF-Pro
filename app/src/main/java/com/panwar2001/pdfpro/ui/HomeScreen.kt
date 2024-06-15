package com.panwar2001.pdfpro.ui

import android.annotation.SuppressLint
import android.os.Environment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.ToolsData
import kotlin.coroutines.coroutineContext
import kotlin.math.absoluteValue


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(onNavigationIconClick:()->Unit,
    navigateTo: (String)->Unit) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val pagerState= rememberPagerState(
        initialPage = 0,
        pageCount = {2}
    )
    var selectedTab by remember { mutableIntStateOf(pagerState.currentPage) }
    LaunchedEffect(selectedTab) {
        pagerState.scrollToPage(selectedTab)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTab=pagerState.currentPage
    }
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
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)) {
                TabRow(selectedTabIndex = selectedTab ) {
                   Tab(selected = selectedTab==0 , onClick = { selectedTab=0 }) {
                       Row(verticalAlignment = Alignment.CenterVertically){
                           Icon(imageVector = Icons.Default.Home, contentDescription = "Home screen")
                           Text(text="Home", modifier = Modifier.padding(10.dp))
                       }
                   }
                    Tab(selected = selectedTab==1 , onClick = { selectedTab=1 }) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(
                                painter = painterResource(id = R.drawable.pdf_icon),
                                modifier = Modifier.size(26.dp),
                                contentDescription = "pdf",
                            )

                            Text(text="PDF Files", modifier = Modifier.padding(10.dp))
                        }
                    }
                }
                HorizontalPager(state = pagerState) {currentPage->
                    if(currentPage==0) {
                        Screen(
                            searchedText = textState.value.text,
                            list = DataSource.FeatureList,
                            navigateTo
                        )
                    }else{
                        Row {
                            PDFfilesScreen()
                        }
                    }
                }
            }
        }
    val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

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

@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    placeHolder: String,
    modifier: Modifier,
    onNavigationIconClick: () -> Unit
) {
    TextField(
        value = state.value,
        onValueChange = {value->
            state.value = value
        },
        modifier
            .fillMaxWidth()
            .background(color = Color.Red.copy(alpha = .0f))
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .border(
                border = BorderStroke(0.dp, Color.Transparent),
                shape = RoundedCornerShape(40.dp)
            ),
        shape = RoundedCornerShape(40.dp),
        placeholder = {
            Text(text = placeHolder)
        },
        leadingIcon = {
            IconButton(onClick = {onNavigationIconClick() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Navigation Menu"
                )
            }
        },
        trailingIcon = {
            Icon(modifier=modifier.clickable {
                if(state.value.text.isNotEmpty()){
                    state.value= TextFieldValue("")
                }
            },
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear")
        },
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 20.sp
        )
    )
}

@Preview
@Composable
fun Preview(){
    HomeScreen({}) {

    }
}

