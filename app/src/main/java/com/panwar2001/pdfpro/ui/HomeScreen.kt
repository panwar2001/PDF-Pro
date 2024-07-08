package com.panwar2001.pdfpro.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.ToolsData
import com.panwar2001.pdfpro.ui.components.BottomIconButton
import kotlinx.coroutines.launch

data class PdfRow(
    val dateModified: String,
    val name: String,
    val size: Float,
    val id: Long
)

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigationIconClick:()->Unit,
               pdfList: List<PdfRow>,
               navigateTo: (String)->Unit,
               query:String,
               pagerState: PagerState,
               onQueryChange:(String)->Unit,
               onSearch:(String)->Unit,
               active:Boolean,
               onActiveChange:(Boolean)->Unit,
               loading:Boolean,
               showBottomSheet:Boolean,
               setBottomSheetState:(Boolean)->Unit,
               shareFile:(Long)->Unit,
               onPdfCardClick:(Long)->Unit,
               options: List<Int> ,
               scrollToPage:(Int)->Unit,
               setSortBy: (Int) -> Unit,
               toggleSortOrder: () -> Unit,
               sortBy: Int,
               topSearchCount:Int=7,
               onSearchTrailingIconClick:()->Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {HomeScreenTopAppBar(scrollBehavior)},
        bottomBar = { HomeScreenBottomBar(scrollToPage = scrollToPage,
                                          pagerState = pagerState )},
        floatingActionButton = { if(pagerState.currentPage==1)FloatingBottomSheetButton {setBottomSheetState(true)}}
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HomeScreenSearchBar(
                onNavigationIconClick = onNavigationIconClick,
                pdfList = pdfList.take(topSearchCount),
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                active = active,
                onActiveChange=onActiveChange,
                onSearchTrailingIconClick=onSearchTrailingIconClick)

            HorizontalPager(state = pagerState) { currentPage ->
                if (currentPage == 0) {
                    ComposeTools(searchedText = query,
                                 navigateTo=navigateTo)
                }else{
                    PdfFilesScreen(listPDF = pdfList ,
                                   shareFile = shareFile,
                                   onPdfCardClick=onPdfCardClick)
                    if (showBottomSheet) {
                        BottomSheet(onBottomSheetDismiss = {setBottomSheetState(false)},
                            toggleSortOrder = toggleSortOrder,
                            sortBy=sortBy,
                            setSortBy = setSortBy,
                            options=options)
                    }
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenSearchBar(
    onNavigationIconClick:()->Unit,
    pdfList: List<PdfRow>,
    query:String,
    onQueryChange:(String)->Unit,
    onSearch:(String)->Unit,
    active:Boolean,
    onActiveChange:(Boolean)->Unit,
    onSearchTrailingIconClick:()->Unit)
{
    SearchBar(query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {Text(text = stringResource(id = R.string.search_placeholder))},
        leadingIcon = {LeadingIcon(onNavigationIconClick)},
        trailingIcon = { TrailingIcon(onSearchTrailingIconClick)}){
        LazyColumn {
            items(pdfList){
                Row(Modifier.height(50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Row(
                        Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clickable {
                                onSearch(it.name)
                            },
                        verticalAlignment = Alignment.CenterVertically){
                        Icon(imageVector = Icons.Default.Search,
                            modifier = Modifier.padding(start=10.dp),
                            contentDescription = null)
                        Text(text = it.name,
                            fontSize = 18.sp ,
                            modifier = Modifier.padding(start=10.dp))
                    }
                    IconButton(onClick = { onQueryChange(it.name)}) {
                        Icon(imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null)
                    }
                }
                HorizontalDivider()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopAppBar(scrollBehavior:TopAppBarScrollBehavior){
        TopAppBar(title = { Text("Pdf Pro",
                              color = Color.Red,
                              fontWeight = FontWeight.Bold,
                              fontSize = 32.sp)},
            navigationIcon = {
                Icon(painter = painterResource(id = R.mipmap.launcher_icon_foreground),
                    modifier = Modifier.size(70.dp),
                    contentDescription = null,
                    tint = Color.Red)
            },
            scrollBehavior=scrollBehavior)
}
@Composable
fun FloatingBottomSheetButton(onClick: () -> Unit){
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color.White,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_sort_24),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun LeadingIcon(onNavigationIconClick: () -> Unit){
    IconButton(onClick = onNavigationIconClick) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = stringResource(id = R.string.menu)
        )
    }
}
@Composable
fun TrailingIcon(onClick:()->Unit){

    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.Clear,
            contentDescription = stringResource(id = R.string.close)
        )
    }
}

/**
 *  Composable that iterates through various tools and display them on a card
 *
 *  @param searchedText the tool  searched via SearchView by the user
 *  the list of tools which match with the [searchedText]
 */
@Composable
fun ComposeTools(searchedText:String,
           navigateTo: (String)->Unit) {
    val context= LocalContext.current
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(20.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = DataSource.ToolsList.filter {
            context.getString(it.title).contains(searchedText, ignoreCase = true)
        }, key = {it.title}) {item ->
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
fun Card(item: ToolsData,
         modifier:Modifier=Modifier,
         navigateTo: (String)->Unit){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = Modifier
            .size(width = 10.dp, height = 130.dp)
            .padding(10.dp)
            .clickable { navigateTo(item.route) }
        ,colors = CardDefaults.cardColors(
            containerColor =Color.White,
        )
    ) {
        Text(
            text = stringResource(id = item.title),
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
            contentDescription = stringResource(id = item.title),
            modifier= modifier
                .size(width = 40.dp, height = 40.dp)
                .padding(vertical = 1.dp)
                .align(Alignment.CenterHorizontally),
            tint=Color.Red
        )
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenBottomBar(scrollToPage: (Int) -> Unit,
                        pagerState: PagerState){
    Row(Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ){
        BottomIconButton(
            onToggle = {scrollToPage(0)},
            icon = Icons.Outlined.Home,
            text = stringResource(R.string.home),
            highlightButton = pagerState.currentPage==0
        )
        BottomIconButton(
            onToggle = {scrollToPage(1)},
            icon = R.drawable.pdf_icon,
            text = stringResource(R.string.pdf_files_tab),
            highlightButton = pagerState.currentPage==1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onBottomSheetDismiss:()->Unit,
                toggleSortOrder:()->Unit,
                sortBy:Int,
                setSortBy:(Int)->Unit,
                options:List<Int>){
    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        onDismissRequest = onBottomSheetDismiss
    ) {
        Row {
            Text(stringResource(id = R.string.sort_options),
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold)
            AssistChip(
                onClick = toggleSortOrder,
                label = {Text(
                    text= stringResource(id = R.string.change_order),
                    fontWeight = FontWeight.Bold
                )},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_sort),
                        contentDescription =null,
                        modifier= Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )
        }
        LazyColumn {
            items(options) { option ->
                Column {
                    HorizontalDivider(thickness = 2.dp)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    ) {
                        RadioButton(selected = option == sortBy,
                            enabled = option != sortBy,
                            onClick = {setSortBy(option)})
                        Text(text = stringResource(id = option))
                        Spacer(modifier=Modifier.width(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PdfFilesScreen(listPDF: List<PdfRow>,
                   shareFile:(Long)->Unit,
                   onPdfCardClick:(Long)->Unit,
) {
    LazyColumn {
        items(listPDF) { pdfItem ->
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable { onPdfCardClick(pdfItem.id) },
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Image(painter = painterResource(id = R.drawable.pdf_svg),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp))
                    Column {
                        Row(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween){
                            Text(
                                text = pdfItem.name,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier=Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.Share,
                                modifier = Modifier.clickable {shareFile(pdfItem.id)},
                                contentDescription = null
                            )
                        }
                        Row{
                            Text("${pdfItem.size} MB")
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(pdfItem.dateModified)
                        }
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun Preview(){
    val pdfList= mutableListOf<PdfRow>()
    repeat(20) {
        pdfList.add(PdfRow("18/03/02", "file_${it}.pdf", 2f, 0))
    }
    var query by remember{ mutableStateOf("") }
    var active by remember{ mutableStateOf(false) }
    val pagerState= rememberPagerState{2}
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope= rememberCoroutineScope()
    val options = listOf(
        R.string.sort_by_date,
        R.string.sort_by_size,
        R.string.sort_by_name)
    var sortBy by  remember {
        mutableIntStateOf(options[0])
    }
    HomeScreen(
        onNavigationIconClick = { },
        pdfList = pdfList,
        navigateTo = {},
        query = query,
        pagerState = pagerState,
        onQueryChange = {query=it},
        onSearch = {
            query=it
            active=false
        },
        active = active,
        onActiveChange ={ active=it} ,
        loading = false,
        showBottomSheet = showBottomSheet,
        setBottomSheetState = {showBottomSheet=it},
        shareFile = {},
        onPdfCardClick = {},
        scrollToPage = {if(it!=pagerState.currentPage)scope.launch { pagerState.animateScrollToPage(it)}},
        setSortBy = {sortBy=it},
        sortBy = options[0],
        options = options,
        toggleSortOrder = {},
        onSearchTrailingIconClick = {})

}

