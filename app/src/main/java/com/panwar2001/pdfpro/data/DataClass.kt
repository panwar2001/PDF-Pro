package com.panwar2001.pdfpro.data

import android.net.Uri
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 *  This data class represents the tool data mentioned in home screen
 *
 *  @property iconId stores icon id of resources
 *  @property title stores description of a particular tool
 */
data class ToolsData(
    val iconId: Int,
    val title: Int,
    val route: String
)



/**
 * navigation.kt
 * enum values that represent the screens in the app
 */

sealed class Screens(val route:String) {
    data object OnBoard : Screens("onboard")
    data object Home: Screens("home")
    data object  FilePicker: Screens("pick")
    data object LanguagePickerScreen: Screens("language_picker")
    data object PdfViewer: Screens("pdfViewer")
    data object PdfToText: Screens("Pdf2Txt") {
        data object PreviewFile : Screens("Pdf2txt_Preview")
        data object TextScreen: Screens("textScreen")
        data object PdfViewer: Screens("pdf2txt_pdfViewer")
    }
    data object PdfToImage: Screens("Pdf2Img") {
        data object PreviewFile : Screens("Pdf2img_Preview")
        data object PdfViewer: Screens("pdf2img_pdfViewer")
        data object ImageScreen:Screens("imageScreen")
    }
    data object  Img2Pdf:Screens("Img2Pdf"){
        data object PdfViewer: Screens("img2pdf_pdfViewer")
        data object ImagesViewScreen: Screens("images")
        data object  ReorderScreen: Screens("reorder")
    }
}

/**
 *  @property toolDescription
 *  @property buttonDescription
 */
data class Tool(
    val toolDescription:Int,
    val buttonDescription:Int
)

data class Spacing(
    val default: Dp=0.dp,
    val extraSmall: Dp=4.dp,
    val small: Dp=8.dp,
    val medium: Dp=16.dp,
    val large: Dp=32.dp,
    val extraLarge: Dp=64.dp
)

data class ImageInfo(
    val uri: Uri = Uri.EMPTY,
    val type:String="",
    val size:Float=0f,
    val checked:Boolean=false
)
