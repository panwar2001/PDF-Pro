package com.panwar2001.pdfpro.ui

import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun PdfFilesScreen() {
    val context = LocalContext.current
    Environment.getRootDirectory()
    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.DATE_MODIFIED,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.TITLE,
        MediaStore.Files.FileColumns.SIZE
    )

    val mimeType = "application/pdf"
    val whereClause = "${MediaStore.Files.FileColumns.MIME_TYPE} IN ('$mimeType')"
    val orderBy = "${MediaStore.Files.FileColumns.SIZE} DESC"

    val cursor: Cursor? = context.contentResolver.query(
//        MediaStore.Files.getContentUri("external"),
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        whereClause,
        null,
        orderBy
    )

    cursor?.use {
        if (it.moveToFirst()) {
            Column {
                val idCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val mimeCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
                val addedCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
                val modifiedCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
                val nameCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val titleCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
                val sizeCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)

                do {
                    val fileUri = Uri.withAppendedPath(
                        MediaStore.Files.getContentUri("external"),
                        it.getString(idCol)
                    )
                    val mime = it.getString(mimeCol)
                    val dateAdded = it.getLong(addedCol)
                    val dateModified = it.getLong(modifiedCol)
                    val name = it.getString(nameCol)
                    val title = it.getString(titleCol)
                    val size = it.getLong(sizeCol)

                    PDF(name)
                    Log.e("PDF Info", "Name: $name, Added: $dateAdded, Modified: $dateModified")
                } while (it.moveToNext())
            }
        }
    }
    cursor?.close()
}

@Composable
fun PDF(name: String) {
    Text(text = name)
}
