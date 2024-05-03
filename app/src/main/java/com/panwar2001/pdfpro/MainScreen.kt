package com.panwar2001.pdfpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.ui.theme.PDFProTheme


class MainScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PDFProTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    NewList()
                }
            }
        }
    }
}


@Composable
fun NewList(){
    val list = listOf(
        "Pdf to text",
        "Pdf to images",
        "Ocr pdf",
        "Images to pdf",
        "Unlock pdf",
        "Summarize pdf",
        "Compress pdf",
        "Searchable pdf",
        "word to pdf",
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BackgroundDesign()
        Screen(list = list)
    }
}
@Composable
fun Screen(modifier: Modifier = Modifier, list: List<String>) {
    Column(modifier.fillMaxSize()) {
        val textState = remember {
            mutableStateOf(TextFieldValue(""))
        }
        SearchView(state = textState, placeHolder = "Search here...", modifier = modifier)

        val searchedText = textState.value.text

        LazyVerticalGrid(columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp)
        ) {
            items(items = list.filter {
                it.contains(searchedText, ignoreCase = true)
            }, key = {it}) {item ->
                Card(item = item)
            }
        }
    }
}

@Composable
fun Card(item:String,modifier:Modifier=Modifier){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = Modifier
            .size(width = 10.dp, height = 130.dp)
            .padding(10.dp)
            .clickable { }
        ,colors = CardDefaults.cardColors(
            containerColor =Color.White,
        )
    ) {
        Text(
            text = item,
            textAlign = TextAlign.Center, // make text center horizontal
            modifier = modifier
                .width(150.dp)
                .height(80.dp)
                .padding(5.dp)
                .wrapContentHeight(),// make text center vertical
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = R.drawable.ion_images),
            contentDescription = "text icon",
            modifier= modifier
                .size(width = 30.dp, height = 30.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    placeHolder: String,
    modifier: Modifier
) {
    TextField(
        value = state.value,
        onValueChange = {value->
            state.value = value
        },
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.Red.copy(alpha = .0f))
            .padding(horizontal = 40.dp, vertical = 10.dp)
            .border(border = BorderStroke(1.dp, Color.Black)),
        placeholder = {
            Text(text = placeHolder)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search,
                contentDescription ="Search Icon",
                tint=Color.Black)
        },
        trailingIcon = {
            Icon(modifier=modifier.clickable {
                if(state.value.text.isNotEmpty()){
                    state.value=TextFieldValue("")
                }
            },
                tint =Color.Black,
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear")
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        shape = MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(
            color = Color.Black, fontSize = 20.sp
        )
    )
}


