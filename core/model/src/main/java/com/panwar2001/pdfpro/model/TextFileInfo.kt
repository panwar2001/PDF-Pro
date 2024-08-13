package com.panwar2001.pdfpro.model

import android.net.Uri

data class TextFileInfo(
    val fileName: String,
    val id: Long,
    val uri: Uri,
    val fileSize:String)
