package com.panwar2001.pdfpro.data

import android.net.Uri
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

sealed class Screens(val route: String) {
    data object onBoard : Screens("onboard")
    data object home: Screens("home")
    data object  FilePicker: Screens("pick")
    data object PdfToText: Screens("PdfToText") {
        data object previewFile : Screens("txtPdfPreview")
        data object TextScreen: Screens("textScreen")
        data object PdfDisplay: Screens("PdfDisplayTxt")
    }
    data object PdfToImage: Screens("PdfToImage") {
        data object PreviewFile : Screens("imgPdfPreview")
        data object PdfDisplay: Screens("pdfDisplayImg")
        data object ImageScreen:Screens("imageScreen")
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


data class Spacing(
    val default: Dp=0.dp,
    val extraSmall: Dp=4.dp,
    val small: Dp=8.dp,
    val medium: Dp=16.dp,
    val large: Dp=32.dp,
    val extraLarge: Dp=64.dp
)