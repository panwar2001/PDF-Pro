package com.panwar2001.pdfpro.data

import android.net.Uri
import androidx.compose.ui.graphics.Color

/**
 *  This data class represents the tool data mentioned in home screen
 *
 *  @property iconId stores icon id of resources
 *  @property title stores description of a particular tool
 */
data class ToolsData(
    val iconId: Int,
    val title: Int,
    val route: String,
    val iconColor: Color=Color.Blue
)

/**
 *  @property toolDescription
 *  @property buttonDescription
 */
data class Tool(
    val toolDescription:Int,
    val buttonDescription:Int
)

data class ImageInfo(
    val uri: Uri = Uri.EMPTY,
    val type:String="image/jpeg",
    val size:String="",
    val checked:Boolean=false
)
