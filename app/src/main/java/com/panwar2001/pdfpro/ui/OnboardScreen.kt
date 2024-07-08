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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.ui.components.DevicePreviews
import com.panwar2001.pdfpro.ui.theme.PDFProTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class OnBoardData(
    val icon: Int,
    val title:Int,
    val description:Int
)
val OnBoardList= listOf(
    OnBoardData(icon=R.drawable.ocr,
                title=R.string.ocr_pdf,
                description = R.string.ocr_description),
    OnBoardData(icon=R.drawable.pdf_compresser,
                title=R.string.compress_pdf,
                description = R.string.compress_pdf_description),
    OnBoardData(icon=R.drawable.lock_pdf,
                title=R.string.lock_pdf,
                description = R.string.lock_description)
)


/**
 * @param navigateTo function that helps to navigate to a screen
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardScreen( navigateTo:(String)->Unit) {
    val pagerState = rememberPagerState( pageCount = {OnBoardList.size})
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
                 .padding(dimensionResource(id = R.dimen.spacing_large))
                 .fillMaxHeight(),
             horizontalAlignment = Alignment.CenterHorizontally,
             verticalArrangement = Arrangement.SpaceBetween
         ) {
             Skip(navigateTo)
             DisplayImage(page = page)
             Title(page = page)
             Description(page = page)
             PageIndicator(
                 pageCount = OnBoardList.size,
                 currentPage = pagerState.currentPage)
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
                pageCount = OnBoardList.size,
                currentPage = pagerState.currentPage
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
          if(pagerState.currentPage+1==OnBoardList.size){
              navigateTo(Screens.Home.route)
          }else {
              CoroutineScope(Dispatchers.Main).launch {
                  pagerState.scrollToPage(pagerState.currentPage + 1)
              }
          } },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.spacing_xxlarge)),
        colors=ButtonDefaults.buttonColors(colorResource(id = R.color.primary))
    ) {
        Text(
            text = stringResource(id =if(OnBoardList.size!=page+1) R.string.next else R.string.started),
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            letterSpacing = MaterialTheme.typography.titleLarge.letterSpacing,
            textDecoration = MaterialTheme.typography.titleLarge.textDecoration,
            color = Color.White
        )
    }
}

@Composable
fun Title(page:Int){
    Text(
        text = stringResource(id = OnBoardList[page].title),
        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
        fontWeight = FontWeight.Bold,
        overflow= TextOverflow.Clip,
        textAlign = TextAlign.Center,
        fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        letterSpacing = MaterialTheme.typography.titleLarge.letterSpacing,
        textDecoration = MaterialTheme.typography.titleLarge.textDecoration,
        modifier= Modifier
            .wrapContentSize()
    )
}
@Composable
fun DisplayImage(page:Int){
    Image(
        painter = painterResource(id = OnBoardList[page].icon),
        contentDescription = stringResource(id = OnBoardList[page].title),
        modifier = Modifier
            .wrapContentSize()
            .padding(dimensionResource(id = R.dimen.spacing_large))
            .size(dimensionResource(id = R.dimen.onboard_image_dimen)),
    )
}

@Composable
fun Description(page:Int){
    Spacer(Modifier.height(dimensionResource(id = R.dimen.spacing_small)))
    Text(
        text = stringResource(id = OnBoardList[page].description),
        overflow= TextOverflow.Clip,
        textAlign = TextAlign.Center,
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
        fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
        fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
        letterSpacing = MaterialTheme.typography.titleMedium.letterSpacing,
        textDecoration = MaterialTheme.typography.titleMedium.textDecoration)
}

@Composable
fun Skip(navigateTo: (String) -> Unit){
    Row(horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "${stringResource(id = R.string.skip)}→",
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
            fontFamily = MaterialTheme.typography.headlineSmall.fontFamily,
            fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
            letterSpacing = MaterialTheme.typography.headlineSmall.letterSpacing,
            textDecoration = MaterialTheme.typography.headlineSmall.textDecoration,
            modifier = Modifier.clickable {
                navigateTo(Screens.Home.route)
            }
        )
        MaterialTheme.colorScheme.background
    }
}


@Composable
fun PageIndicator(pageCount: Int, currentPage: Int) {
    val dimIfSelected=dimensionResource(id = R.dimen.spacing_moreLarge)
    val dimIfNotSelected=dimensionResource(id = R.dimen.spacing_large)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(pageCount){
            val isSelected = it == currentPage
            val width = animateDpAsState(targetValue = if (isSelected) dimIfSelected else dimIfNotSelected, label = "")
            Box(modifier = Modifier
                .padding(dimensionResource(id = R.dimen.spacing_tiny))
                .height(dimIfNotSelected)
                .width(width.value)
                .clip(CircleShape)
                .alpha(if (isSelected) 0.9f else 0.5f)
                .background(if (MaterialTheme.isLight()) Color.DarkGray else Color.LightGray)
            )
        }
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_none)))
}
@Composable
fun MaterialTheme.isLight() = this.colorScheme.background.luminance() > 0.5

@DevicePreviews
@Composable
fun LightModeOnBoardingScreenPreview(){
    PDFProTheme(darkTheme =false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            OnboardScreen {}
        }
    }
}

@DevicePreviews
@Composable
fun DarkModeOnBoardingScreenPreview(){
    PDFProTheme(darkTheme =true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            OnboardScreen {}
        }
    }
}

