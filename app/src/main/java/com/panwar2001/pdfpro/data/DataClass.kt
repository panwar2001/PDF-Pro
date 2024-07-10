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
