package com.panwar2001.pdfpro.compose.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider

fun sharePdfFile(context:Context,
              fileURI:Uri,
              fileMimeType:String){
 val shareIntent= Intent().apply {
     action=Intent.ACTION_SEND
     type=fileMimeType
     putExtra(Intent.EXTRA_STREAM,fileURI)
     addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
 }
    context.startActivity(Intent.createChooser(shareIntent,"Share PDF"))
}