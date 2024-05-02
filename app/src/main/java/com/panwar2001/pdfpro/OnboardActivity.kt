package com.panwar2001.pdfpro
//icons: https://fonts.google.com/icons
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.panwar2001.pdfpro.ui.theme.PDFProTheme

class OnboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PDFProTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CommonLayout()
                }
            }
        }
    }
}


@Composable
fun HandleOrientationChanges() {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation==Configuration.ORIENTATION_LANDSCAPE
    if (isLandscape) {
        LandscapeLayout()
    } else {
        PortraitLayout()
    }
}

@Composable
fun CommonLayout( modifier:Modifier = Modifier) {
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {context.startActivity(Intent(context,MainScreen::class.java)) },
                shape = CircleShape,
                containerColor = Color.White,
            ) {
                Icon(
                    Icons.Filled.ArrowForward, "Large floating action button",
                    modifier = modifier.size(50.dp),
                    tint = Color.Red
                )
            }
        }, bottomBar = {
            BottomAppBar(
                containerColor = Color.White
            ) {
                BannerAd()
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            HandleOrientationChanges()
        }

    }
}

@Composable
fun PortraitLayout( modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.resource_try),
            contentDescription = "Rectangle curved design",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth(1f),
        )
        Text(
            text = stringResource(id = R.string.onboard_title),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            ),
            modifier = modifier.padding(5.dp, 40.dp), color = Color.White
        )
    }
    Image(
        painter = painterResource(id = R.drawable.pdf_svg),
        contentDescription = "pdf svg",
        modifier=modifier.padding(40.dp,5.dp)
    )
}

@Composable
fun BannerAd(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/9214589741"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
@Composable
fun LandscapeLayout(modifier: Modifier=Modifier){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
            ) {
                Text(
                    text = stringResource(id = R.string.onboard_title),
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    ),
                    modifier = modifier.padding(10.dp, 10.dp), color = Color.White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.pdf_svg),
                contentDescription = "pdf svg",
                modifier=modifier.padding(40.dp,30.dp)
            )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    PDFProTheme {
        CommonLayout()
    }
}