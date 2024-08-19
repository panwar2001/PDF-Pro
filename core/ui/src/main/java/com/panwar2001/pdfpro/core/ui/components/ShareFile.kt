package com.panwar2001.pdfpro.core.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * [sharePdfFile] to share the file provided it's mimetype and the uri
 *
 * @param context the main ui thread context, provided to share the file
 * @param fileURI the uri of the file
 * @param fileMimeType the mime type of the file to be shared
 */
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