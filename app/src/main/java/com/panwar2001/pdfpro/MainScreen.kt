package com.panwar2001.pdfpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    Box {
                            Screen()
                    }
                }
            }
        }
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier) {
    val data = listOf("Pdf to text", "Pdf to images", "Ocr pdf", "Images to pdf", "Word to pdf", "Searchable pdf", "Compress pdf", "Summarize pdf")
   LazyVerticalGrid(columns = GridCells.Fixed(2),
       contentPadding = PaddingValues(20.dp)
   ) {
      items(data){item->
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
                      modifier = modifier.width(150.dp)
                          .height(80.dp).padding(5.dp)
                          .wrapContentHeight(),// make text center vertical
                      fontWeight = FontWeight.Bold
                  )
              Image(
                  painter = painterResource(id = R.drawable.ion_images),
                  contentDescription = "text icon",
                  modifier=modifier.size(width = 30.dp,height=30.dp).align(Alignment.CenterHorizontally)
              )
          }
      }
   }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PDFProTheme {
        Screen()
    }
}
