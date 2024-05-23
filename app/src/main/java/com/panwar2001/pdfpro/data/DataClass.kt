package com.panwar2001.pdfpro.data

import android.net.Uri

/**
 *  This data class represents the tool data mentioned in home screen
 *
 *  @property iconId stores icon id of resources
 *  @property description stores description of a particular tool
 *  @property key it is the unique identifier of specific tool from tools list
 */
data class ToolsData(
    val iconId: Int,
    val description: String,
    val key :Int,
    val screen: String
)

/**
 * This data class is used to store information for menu items of navigation side bar
 *
 * @property screen screen of a particular tool
 * @property title title of the menu item of navigation bar
 * @property contentDescription  describes the icon
 * @property icon the drawable id of icon
 */
data class MenuItem(
    val screen: String,
    val title: String,
    val contentDescription: String,
    val icon: Int
)


/**
 * navigation.kt
 * enum values that represent the screens in the app
 */
//enum class df(@StringRes val title: Int) {
//    Splash(title = R.string.splash),
//    OnBoard(title = R.string.onBoard),
//    Home(title = R.string.home),
//    Pick(title = R.string.pick),
////    PreviewFile(title=R.string.)
//}

sealed class Screens(val route: String) {
    object onBoard : Screens("onboard")
    object home: Screens("home")
    object PdfToText: Screens("PdfToText") {
        object  FilePicker: Screens("pick")
        object previewFile : Screens("previewFile")
    }
    object PdfToImage: Screens("PdfToImage") {
        object  FilePicker: Screens("pick")
        object previewFile : Screens("previewFile")
    }
}

/**
 *  @property toolDescription
 *  @property buttonDescription
 */
data class Tool(
    val toolDescription:String,
    val buttonDescription:String
)

data class OnBoardData(
    val title:String,
    val description:String,
    val icon: Int
)


/**
 * Data class that represents the current UI state in terms of [id] and [uri]
 */
data class PdfProUiState(
   val id : Int=0,
   val uri: Uri=Uri.EMPTY
)

