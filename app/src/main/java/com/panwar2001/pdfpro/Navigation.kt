package com.panwar2001.pdfpro
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.ui.HomeScreen
import com.panwar2001.pdfpro.ui.LanguagePickerScreen
import com.panwar2001.pdfpro.ui.OnboardScreen
import com.panwar2001.pdfpro.ui.components.DrawerBody
import com.panwar2001.pdfpro.ui.components.DrawerHeader
import com.panwar2001.pdfpro.ui.theme.PDFProTheme
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import kotlinx.coroutines.launch


class Navigation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PDFBoxResourceLoader.init(applicationContext)
        if(hasExternalStoragePermission()){
            pdfProContent()
        }else{
            requestPermission()
        }
    }

    private fun pdfProContent(){
        setContent{
            val theme= remember {
                mutableStateOf(isDarkTheme())
            }
            PDFProTheme(darkTheme =theme.value) {
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
                        },
                        setTheme={
                            val sharedPreferences = this.getSharedPreferences("Theme", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("theme", it)
                            editor.apply()
                            theme.value=it
                        },
                        currentTheme=theme.value)
                }
            }
        }
    }

    /**
     * If onboarding is done once when app is run for first time then always
     * home screen is opened after splash screen.
     */
    private fun getStartDestination( ): String {
        val sharedPreferences = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val onBoardingIsFinished= sharedPreferences.getBoolean("isFinished", false)
        return if(onBoardingIsFinished) Screens.Home.route else Screens.OnBoard.route
    }
    private fun isDarkTheme():Boolean{
        val sharedPreferences = this.getSharedPreferences("Theme", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("theme", false)
    }
    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + this.packageName)
                startActivity(intent)
            }
        } else {
            val permissionRequestCode=1
            // For devices below Android 11, request storage permissions normally
            // (e.g., WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
            val permissionsNeeded = mutableListOf<String>()

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (permissionsNeeded.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), permissionRequestCode)
            }
        }
    }
    private fun hasExternalStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
//            val hasReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
              val hasWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
             hasWritePermission
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val permissionRequestCode=1
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                pdfProContent()
            } else {
                // Permissions are denied
                requestPermission()
            }
        }
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
    navController: NavHostController= rememberNavController(),
    startDestination:String,
    setOnboardingFinished:()->Unit,
    setTheme:(Boolean)->Unit,
    currentTheme:Boolean
)
{

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // used for navigation through navigation drawer
    val navigateTo:(String)->Unit={
    navController.navigate(it){
        if(drawerState.isOpen){
            scope.launch {drawerState.apply {close()}}
        }
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // selecting the same item
        launchSingleTop = true
        // Restore state when selecting a previously selected item
        restoreState = true
    }
}
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(Modifier.padding(0.dp,0.dp,60.dp,0.dp)) {
                DrawerHeader()
                DrawerBody(items = DataSource.ToolsList,
                           setTheme=setTheme,
                           currentTheme=currentTheme){ navigateTo(it)}
              }
        },
        gesturesEnabled = drawerState.isOpen)
    {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize(),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            )
            {

                composable(route = Screens.OnBoard.route) {
                    OnboardScreen {
                        // lambda function for navigation
                        setOnboardingFinished()
                        navController.popBackStack()
                        navController.navigate(it)
                    }
                }

                composable(route = Screens.Home.route) {
                    HomeScreen(onNavigationIconClick = {
                        scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                    }) {    // lambda function for navigation
                        navigateTo(it)
                    }
                }
                composable(route= Screens.LanguagePickerScreen.route){
                    LanguagePickerScreen(navigateUp={
                        navController.navigateUp()
                    })
                }
                pdf2txtGraph(navController=navController,
                    scope=scope,
                    drawerState=drawerState)

                pdf2ImgGraph(navController=navController,
                    scope=scope,
                    drawerState=drawerState)

                img2PdfGraph(navController=navController,
                    scope=scope,
                    drawerState=drawerState)
            }
        }
    }
}




/**
 *  Extend the nav back stack entry function to create sharedViewModel Instance with it.
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController:NavController): T {

    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}