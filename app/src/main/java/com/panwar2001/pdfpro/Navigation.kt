package com.panwar2001.pdfpro

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.panwar2001.pdfpro.ui.theme.PDFProTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.Screens
import com.panwar2001.pdfpro.ui.HomeScreen
import com.panwar2001.pdfpro.ui.OnboardScreen
import com.panwar2001.pdfpro.ui.components.DrawerBody
import com.panwar2001.pdfpro.ui.components.DrawerHeader
import com.panwar2001.pdfpro.ui.components.FilePickerScreen
import com.panwar2001.pdfpro.ui.components.PdfViewer
import com.panwar2001.pdfpro.ui.components.ProgressIndicator
import com.panwar2001.pdfpro.ui.pdfToText.PreviewFileScreen
import com.panwar2001.pdfpro.ui.pdfToText.TextScreen
import com.panwar2001.pdfpro.ui.view_models.PdfToImagesViewModel
import com.panwar2001.pdfpro.ui.view_models.PdfToTextViewModel
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import kotlinx.coroutines.launch


class Navigation : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PDFBoxResourceLoader.init(applicationContext)
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
    private fun getStartDestination( ): String {
        val sharedPreferences = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val onBoardingIsFinished= sharedPreferences.getBoolean("isFinished", false)
        return if(onBoardingIsFinished) Screens.home.route else Screens.onBoard.route
    }
    private fun isDarkTheme():Boolean{
        val sharedPreferences = this.getSharedPreferences("Theme", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("theme", false)
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
                DrawerBody(items = DataSource.MenuItems,setTheme=setTheme,currentTheme=currentTheme){
                    navigateTo(it)
                }
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

                composable(route = Screens.onBoard.route) {
                    OnboardScreen {
                        // lambda function for navigation
                        setOnboardingFinished()
                        navController.popBackStack()
                        navController.navigate(it)
                    }
                }

                composable(route = Screens.home.route) {
                    HomeScreen(onNavigationIconClick = {
                        scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                    }) {    // lambda function for navigation
                        navigateTo(it)
                    }
                }
                navigation(route=Screens.PdfToText.route,startDestination=Screens.PdfToText.FilePicker.route){
                    composable(route=Screens.PdfToText.FilePicker.route){ model->
                            val viewModel = model.sharedViewModel<PdfToTextViewModel>(navController)
                            FilePickerScreen(onNavigationIconClick = {
                                scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                            },
                                setUri = {uri:Uri-> viewModel.setUri(uri)},
                                setLoading={loading:Boolean->viewModel.setLoading(loading)},
                                navigate = {
                                    navController.navigate(Screens.PdfToText.previewFile.route) {
                                        if (drawerState.isOpen) {
                                            scope.launch { drawerState.apply { close() } }
                                        }
                                    }
                                },selectMultipleFile = false,
                                mimeType = "application/pdf",
                                tool=DataSource.getToolData(0))
                    }
                    composable(route=Screens.PdfToText.previewFile.route) {model->
                        val viewModel = model.sharedViewModel<PdfToTextViewModel>(navController)
                        val uiState by viewModel.uiState.collectAsState()
                        if (uiState.isLoading) {
                            ProgressIndicator(modifier = Modifier)
                            viewModel.generateThumbnailFromPDF(LocalContext.current)
                        } else {
                            PreviewFileScreen(
                                onNavigationIconClick = {
                                    scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                                },
                                navigateTo = { dest:String->
                                    navController.navigate(dest) {
                                        if (drawerState.isOpen) {
                                            scope.launch { drawerState.apply { close() } }
                                        }
                                    }
                                },
                                thumbnail = uiState.thumbnail,
                                fileName = uiState.fileName,
                                setLoading={loading:Boolean->viewModel.setLoading(loading)}
                            )
                        }
                    }
                    composable(route=Screens.PdfToText.PdfDisplay.route){model->
                        val viewModel = model.sharedViewModel<PdfToTextViewModel>(navController)
                        val uiState by viewModel.uiState.collectAsState()
                        PdfViewer(uri = uiState.uri, navigateUp ={navController.navigateUp()},uiState.fileName)
                    }
                    composable(route=Screens.PdfToText.TextScreen.route){model->
                        val viewModel = model.sharedViewModel<PdfToTextViewModel>(navController)
                        val uiState by viewModel.uiState.collectAsState()
                        if(uiState.isLoading && uiState.text==""){
                            ProgressIndicator(modifier = Modifier)
                            viewModel.convertToText(LocalContext.current)
                        }else {
                            if(uiState.text!="") {
                                TextScreen(text = uiState.text) {
                                    scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                                }
                            }else{
                                ProgressIndicator(modifier = Modifier)
                            }
                        }
                    }
                }
                navigation(route=Screens.PdfToImage.route,startDestination=Screens.PdfToImage.FilePicker.route){
                    composable(route=Screens.PdfToImage.FilePicker.route){
                        val viewModel = it.sharedViewModel<PdfToImagesViewModel>(navController)
                        FilePickerScreen(onNavigationIconClick = {
                            scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                        },
                            setUri = {uri:Uri-> viewModel.setUri(uri)},
                            setLoading={loading:Boolean->viewModel.setLoading(loading)},
                            navigate = {
                                navController.navigate(Screens.PdfToImage.PreviewFile.route) {
                                    if (drawerState.isOpen) {
                                        scope.launch { drawerState.apply { close() } }
                                    }
                                }
                            },selectMultipleFile = false,
                            mimeType = "application/pdf",
                            tool=DataSource.getToolData(1))
                    }
                    composable(route=Screens.PdfToImage.PreviewFile.route) {model->
                        val viewModel = model.sharedViewModel<PdfToImagesViewModel>(navController)
                        val uiState by viewModel.uiState.collectAsState()
                        if (uiState.isLoading) {
                            ProgressIndicator(modifier = Modifier)
                            viewModel.generateThumbnailFromPDF(LocalContext.current)
                        } else {
                            PreviewFileScreen(
                                onNavigationIconClick = {
                                    scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                                },
                                navigateTo = { dest:String->
                                    navController.navigate(dest) {
                                        if (drawerState.isOpen) {
                                            scope.launch { drawerState.apply { close() } }
                                        }
                                    }
                                },
                                thumbnail = uiState.thumbnail,
                                fileName = uiState.fileName,
                                setLoading={loading:Boolean->viewModel.setLoading(loading)}
                            )
                        }
                    }
                }
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