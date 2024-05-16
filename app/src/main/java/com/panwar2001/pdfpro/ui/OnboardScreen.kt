package com.panwar2001.pdfpro.ui

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @param navigateTo function that helps to navigate to a screen
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardScreen( navigateTo:(String)->Unit) {
    val pagerState = rememberPagerState( pageCount = {DataSource.OnBoardList.size})
    val configuration = LocalConfiguration.current
    if (configuration.orientation==Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeLayout(pagerState = pagerState, navigateTo=navigateTo)
    } else {
        PortraitLayout(pagerState = pagerState, navigateTo=navigateTo)
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PortraitLayout( modifier: Modifier = Modifier,
                    pagerState:PagerState,
                    navigateTo: (String) -> Unit) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        HorizontalPager(state = pagerState,modifier.wrapContentSize()) { page ->
            // Our page content
         Column(
             modifier
                 .padding(25.dp)
                 .fillMaxHeight(),
             horizontalAlignment = Alignment.CenterHorizontally,
             verticalArrangement = Arrangement.SpaceBetween
         ) {
             Skip(navigateTo)
             DisplayImage(page = page)
             Title(page = page)
             Description(page = page)
             PageIndicator(
                 pageCount = DataSource.OnBoardList.size,
                 currentPage = pagerState.currentPage,
                 modifier = Modifier.padding(0.dp,0.dp,0.dp,30.dp)
             )
             NextButton(pagerState = pagerState, page = page,navigateTo)
         }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LandscapeLayout(modifier: Modifier=Modifier,
                    pagerState: PagerState,
                    navigateTo: (String) -> Unit){
    HorizontalPager(state = pagerState, modifier.wrapContentSize()) { page ->
    Row(modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment =Alignment.CenterVertically
        ) {
        Column(modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            DisplayImage(page = page)
            PageIndicator(
                pageCount = DataSource.OnBoardList.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 30.dp)
            )
        }

        Column(modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Skip(navigateTo)
            Title(page = page)
            Description(page = page)
            NextButton(pagerState = pagerState, page = page,navigateTo)
         }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NextButton(pagerState: PagerState,page:Int,navigateTo: (String) -> Unit){
    Button(
        onClick = {
          if(pagerState.currentPage+1==DataSource.OnBoardList.size){
              navigateTo(Screens.Home.name)
          }else {
              CoroutineScope(Dispatchers.Main).launch {
                  pagerState.scrollToPage(pagerState.currentPage + 1)
              }
          } },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        colors=ButtonDefaults.buttonColors(colorResource(id = R.color.primary))
    ) {
        Text(
            text = if(DataSource.OnBoardList.size!=page+1) "Next" else "Get Started",
            fontSize = 20.sp,
            color = if(isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}

@Composable
fun Title(page:Int){
    Text(
        text = DataSource.OnBoardList[page].title,
        fontSize = 44.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier=Modifier.wrapContentSize()
    )
}
@Composable
fun DisplayImage(page:Int){
    Image(
        painter = painterResource(id = DataSource.OnBoardList[page].icon),
        contentDescription = DataSource.OnBoardList[page].title,
        modifier = Modifier
            .wrapContentSize()
            .padding(40.dp)
            .size(width = 150.dp, 150.dp),
    )
}

@Composable
fun Description(page:Int){
    Text(
        text = DataSource.OnBoardList[page].description,
        modifier = Modifier.padding(top = 45.dp),
        overflow= TextOverflow.Clip,
        textAlign = TextAlign.Center,
        fontSize = 20.sp
    )
}

@Composable
fun Skip(navigateTo: (String) -> Unit){
    Row(horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Skip→",
            textAlign = TextAlign.End,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                navigateTo(Screens.Home.name)
            }
        )
    }
}


@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        repeat(pageCount){
            val isSelected = it == currentPage
            val width = animateDpAsState(targetValue = if (isSelected) 35.dp else 15.dp, label = "")
            Box(modifier = Modifier
                .padding(2.dp)
                .height(15.dp)
                .width(width.value)
                .clip(CircleShape)
                .alpha(if (isSelected) 0.9f else 0.5f)
                .background(if (isSystemInDarkTheme()) Color.White else Color.DarkGray)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true, device = "id:pixel_5")
@Composable
fun OnBoardingScreenPreview(){
    val pagerState = rememberPagerState( pageCount = {DataSource.OnBoardList.size})
    PortraitLayout(pagerState=pagerState){

    }
}