package com.panwar2001.pdfpro.data

import androidx.annotation.StringRes
import com.panwar2001.pdfpro.R
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
    val id: String
)

/**
 * This data class is used to store information for menu items of navigation side bar
 *
 * @property id id of a particular tool
 * @property title title of the menu item of navigation bar
 * @property contentDescription  describes the icon
 * @property icon the drawable id of icon
 */
data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: Int
)


/**
 * navigation.kt
 * enum values that represent the screens in the app
 */
enum class PdfProScreen(@StringRes val title: Int) {
    Splash(title = R.string.splash),
    OnBoard(title = R.string.onBoard),
    Home(title = R.string.home),
    Upload(title = R.string.upload)
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
data class OrderUiState(
   val id : Int=0,
   val uri: Uri=Uri.EMPTY
)

