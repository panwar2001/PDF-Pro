package com.panwar2001.pdfpro.ui

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.ui.components.DevicePreviews
import com.panwar2001.pdfpro.ui.theme.PDFProTheme
import com.panwar2001.pdfpro.ui.view_models.OnBoardData


/**
 * First time screen to user
 *
 * The [OnboardScreen] is displayed to user first time when the app is opened
 * It displays in brief the features and use cases of the app, which enhance user
 * experience.
 *
 * @param navigateToHome navigates screen to home screen
 * @param pagerState  stores state of pager
 * @param onBoardList list of features data
 * @param onNextButtonClick handles logic to either jump to next pager state or to home screen
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun  OnboardScreen( navigateToHome: () -> Unit,
                   pagerState: PagerState,
                   onBoardList:List<OnBoardData>,
                   onNextButtonClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    Column(
        Modifier.padding(dimensionResource(id = R.dimen.spacing_large))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) { 
        Skip(navigateToHome)
        HorizontalPager(state = pagerState,Modifier.wrapContentSize()) { page ->
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           LandscapeLayout(
               imageId = onBoardList[page].icon,
               pageCount = pagerState.pageCount,
               titleId = onBoardList[page].title,
               currentPage = page ,
               descriptionId = onBoardList[page].description
           )
        } else {
            DisplayImage(imageId = onBoardList[page].icon)
            Title(textId = onBoardList[page].title)
            Description(descriptionId = onBoardList[page].description)
            PageIndicator(
                pageCount = pagerState.pageCount,
                currentPage = page)
        }
        NextButton(textId = if(pagerState.pageCount!=pagerState.currentPage)
                           R.string.next else R.string.started,
                   onNextButtonClick=onNextButtonClick)
      }
    }
}

/**
 * landscape screen
 *
 * [LandscapeLayout] composable orients the onboarding screen in landscape layout
 *
 * @param imageId resource id of image
 * @param pageCount the number of pages displayed via horizontal pager
 * @param titleId resource id of title
 * @param currentPage current page of horizontal pager
 * @param descriptionId resource id of description
 */
@Composable
fun  LandscapeLayout(imageId: Int,
                    pageCount: Int,
                    titleId:Int,
                    currentPage: Int,
                    descriptionId: Int){
    Row(Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment =Alignment.CenterVertically
        ) {
        Column(Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            DisplayImage(imageId =imageId)
            PageIndicator(
                pageCount = pageCount,
                currentPage = currentPage)
        }
        Column(Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Title(textId = titleId)
            Description(descriptionId = descriptionId)
         }
    }
}


/**
 * Next button
 *
 * Composable [NextButton] that moves the page to next state of horizontal pager or
 * switch the onboarding screen to home screen
 *
 * @param textId resource id of the text
 * @param onNextButtonClick Move to next page or to the home screen
 */
@Composable
fun NextButton(textId: Int,
               onNextButtonClick:()->Unit){
    Button(
        onClick = onNextButtonClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.spacing_xxlarge)),
        colors=ButtonDefaults.buttonColors(colorResource(id = R.color.primary))
    ) {
        Text(
            text = stringResource(id = textId),
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

/**
 * Displays Title
 *
 * Composable [Title] It  displays title of a particular feature
 *
 * @param textId resource id of the title
 */
@Composable
fun Title(textId:Int){
    Text(
        text = stringResource(id = textId),
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

/**
 * Display App Feature Image
 *
 * Composable [DisplayImage] It is a image component which displays the image relevant to feature of the app
 *
 * @param imageId resource id of the image
 */
@Composable
fun DisplayImage(imageId:Int){
    Image(
        painter = painterResource(id = imageId),
        contentDescription = null,
        modifier = Modifier
            .wrapContentSize()
            .padding(dimensionResource(id = R.dimen.spacing_large))
            .size(dimensionResource(id = R.dimen.onboard_image_dimen)),
    )
}

/**
 * Description of current page
 *
 * [Description] Composable that displays the description of a particular tool on a horizontalPager
 *
 * @param descriptionId resource id of description
 */
@Composable
fun Description(descriptionId:Int){
    Spacer(Modifier.height(dimensionResource(id = R.dimen.spacing_small)))
    Text(
        text = stringResource(id = descriptionId),
        overflow= TextOverflow.Clip,
        textAlign = TextAlign.Center,
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
        fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
        fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
        letterSpacing = MaterialTheme.typography.titleMedium.letterSpacing,
        textDecoration = MaterialTheme.typography.titleMedium.textDecoration)
}

/**
 * Skip to home Screen
 *
 * Composable [Skip] is a clickable text, which on Click help navigate to screen
 *
 * @param navigateToHome function to navigate to home screen
 */
@Composable
fun Skip(navigateToHome: () -> Unit){
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
                navigateToHome()
            }
        )
        MaterialTheme.colorScheme.background
    }
}

/**
 *  indicates page
 *
 *  [PageIndicator] composable indicates the page on the screen
 *
 *  @param pageCount The number of pages
 *  @param currentPage denotes the index of the current page
 */
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

/**
 * check theme
 *
 * Composable [isLight] that checks whether the screen is in light or dark theme
 *
 * @return Boolean value if the screen is light themed or not
 * @receiver [PageIndicator]
 */
@Composable
fun MaterialTheme.isLight() = this.colorScheme.background.luminance() > 0.5



@OptIn(ExperimentalFoundationApi::class)
@DevicePreviews
@Composable
fun OnBoardingScreenPreview(){
    val pagerState= rememberPagerState {3}
    repeat(2) {
        PDFProTheme(darkTheme = it==1) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                OnboardScreen(
                    navigateToHome = { /*TODO*/ },
                    pagerState = pagerState,
                    onBoardList = listOf()
                ) {}
            }
        }
    }
}

