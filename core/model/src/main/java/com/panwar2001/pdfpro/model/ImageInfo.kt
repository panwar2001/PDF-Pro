package com.panwar2001.pdfpro.model

import android.net.Uri

data class ImageInfo(
    val uri: Uri = Uri.EMPTY,
    val type:String="image/jpeg",
    val size:String="",
    val checked:Boolean=false
)
