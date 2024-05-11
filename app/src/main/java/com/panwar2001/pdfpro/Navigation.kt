package com.panwar2001.pdfpro

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.panwar2001.pdfpro.ui.theme.PDFProTheme
import com.panwar2001.pdfpro.ui.view_models.ProViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.panwar2001.pdfpro.data.PdfProScreen
import com.panwar2001.pdfpro.ui.HomeScreen
import com.panwar2001.pdfpro.ui.OnboardScreen
import com.panwar2001.pdfpro.ui.UploadScreen


class Navigation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            PDFProTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationController(
                        startDestination = getStartDestination(),
                        setOnboardingFinished = {
                            val sharedPreferences = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isFinished", true)
                            editor.apply()
                        })
                }
            }
        }
    }
    private fun getStartDestination( ): String {
        val sharedPreferences = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val onBoardingIsFinished= sharedPreferences.getBoolean("isFinished", false)
        return if(onBoardingIsFinished) PdfProScreen.Home.name else PdfProScreen.OnBoard.name
    }
}

/**
 *
 * @author ayush panwar
 * Composable that has navigation host and graph for navigating among different composable screens.
 *
 * @param navController
 */
@Composable
fun NavigationController(
    viewModel: ProViewModel = viewModel(),
    navController: NavHostController= rememberNavController(),
    startDestination:String,
    setOnboardingFinished:()->Unit
)
{
    val resultLauncher=rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val data: Uri? = it.data?.data
        }
    }
    val pdfIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
            .fillMaxSize()
    )
    {
        composable(route=PdfProScreen.OnBoard.name){
           OnboardScreen(
                switchToHome ={
                    setOnboardingFinished()
                    navController.popBackStack()
                    navController.navigate(PdfProScreen.Home.name)
                },
           )
       }
        composable(route=PdfProScreen.Home.name){
           HomeScreen(navController=navController)
        }
        composable(route=PdfProScreen.Upload.name){
            UploadScreen(navController=navController,
                         onUpload={
                             resultLauncher.launch(pdfIntent)
                         })
        }
    }
}
