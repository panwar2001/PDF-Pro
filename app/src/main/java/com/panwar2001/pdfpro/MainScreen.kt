package com.panwar2001.pdfpro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.sideBar.DrawerBody
import com.panwar2001.pdfpro.sideBar.DrawerHeader
import com.panwar2001.pdfpro.sideBar.MenuItem
import com.panwar2001.pdfpro.ui.theme.PDFProTheme
import kotlinx.coroutines.launch


class MainScreen : ComponentActivity() {
    private val list = listOf(
        ToolsData(R.drawable.outline_text,"Pdf to text",0),
        ToolsData(R.drawable.ion_images,"Pdf to images",1),
        ToolsData(R.drawable.compress,"Compress pdf",2),
        ToolsData(R.drawable.ocr,"Ocr pdf",3),
        ToolsData(R.drawable.pdf_icon,"Images to pdf",4),
        ToolsData(R.drawable.unlock_pdf,"lock pdf",5),
        ToolsData(R.drawable.summary_icon,"Summarize pdf",6),
        ToolsData(R.drawable.search_icon,"Searchable pdf",7),
        ToolsData(R.drawable.msword_icon,"Word to pdf",8)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PDFProTheme {
                val scaffoldState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val textState = remember { mutableStateOf(TextFieldValue("")) }

                ModalNavigationDrawer(drawerState = scaffoldState
                    , drawerContent = {
                        ModalDrawerSheet(Modifier.padding(0.dp,0.dp,60.dp,0.dp)){
                            DrawerHeader()
                            DrawerBody(
                                items = listOf(
                                    MenuItem(
                                        id = "home",
                                        title = "Home",
                                        contentDescription = "Go to home screen",
                                        icon = Icons.Default.Home
                                    ),
                                    MenuItem(
                                        id = "settings",
                                        title = "Settings",
                                        contentDescription = "Go to settings screen",
                                        icon = Icons.Default.Settings
                                    ),
                                    MenuItem(
                                        id = "help",
                                        title = "Help",
                                        contentDescription = "Get help",
                                        icon = Icons.Default.Info
                                    ),
                                ),onItemClick = {
                                    println("Clicked on ${it.title}")
                                }
                            )
                        }
                    }) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Scaffold(
                            topBar = {
                                SearchView(state = textState,
                                    placeHolder = "Search here...",
                                    modifier = Modifier,
                                    onNavigationIconClick = {
                                        scope.launch {
                                            scaffoldState.apply {
                                                if(isClosed) open() else close()
                                            }
                                        }
                                    }
                                )

                            },
                        ) {
                                innerPadding->    Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(innerPadding)
                        ) {
                            Column(Modifier.fillMaxSize()) {
                                Screen(searchedText = textState.value.text,list = list)
                            }
                        }
                        }
                    } //Surface end
                }
            }
        }
    }
}






data class ToolsData(
    val iconId: Int,
    val description: String,
    val key :Int
)

@Composable
fun Screen(searchedText:String, list: List<ToolsData>) {
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(20.dp)
    ) {
        items(items = list.filter {
            it.description.contains(searchedText, ignoreCase = true)
        }, key = {it.key}) {item ->
            Card(item =item)
        }
    }
}

@Composable
fun Card(item:ToolsData,modifier:Modifier=Modifier){
    val context = LocalContext.current
    val intent= Intent(context,Upload::class.java)
    intent.putExtra("ID",item.key)
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = Modifier
            .size(width = 10.dp, height = 130.dp)
            .padding(10.dp)
            .clickable {context.startActivity(intent)}
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
                .size(width = 40.dp, height = 40.dp).padding(vertical = 1.dp)
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
            .border(border = BorderStroke(0.dp, Color.Transparent),shape= RoundedCornerShape(40.dp)),
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
                    state.value=TextFieldValue("")
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


