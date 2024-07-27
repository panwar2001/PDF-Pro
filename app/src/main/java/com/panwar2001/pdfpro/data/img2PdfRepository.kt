package com.panwar2001.pdfpro.data

import android.content.Context
import android.net.Uri
import com.panwar2001.pdfpro.view_models.Img2PdfUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface Img2PdfInterface{
   fun initImg2PdfUiState(): Img2PdfUiState
   suspend fun savePdfToExternalStorage(externalStoragePdfUri:Uri,internalStoragePdfUri: Uri)
}

@Singleton
class Img2PdfRepository @Inject constructor(@ApplicationContext private val context: Context): Img2PdfInterface{
    override fun initImg2PdfUiState(): Img2PdfUiState{
        return Img2PdfUiState(
            imageList = listOf(),
            isLoading = false,
            fileName  = "file",
            fileUri   = Uri.EMPTY,
            numPages  = 0
        )
    }

    override suspend fun savePdfToExternalStorage(externalStoragePdfUri: Uri, internalStoragePdfUri: Uri) {
        val inputStream= File(context.filesDir, "file.pdf").inputStream()
        val buf = ByteArray(8192)
        var length: Int
        /**
         *  Sol: Reading input stream from file.pdf from app storage and  a buffer of 8KB is created.
         *  The write operation is done on output stream by writing 8KB of buffered data until
         *  the file ends.
         *  TODO
         *  the way to use internalStoragePdfUri didn't worked as the uri is  from
         *  FileProvider and it writing the data from it is generating a corrupted pdf on
         *  output stream. Find out a way to read and write pdf data from content uri generated
         *  from FileProvider to output stream.
         */
        context.contentResolver.apply {
            openOutputStream(externalStoragePdfUri,"w")?.use { outputStream->
                        while ((inputStream.read(buf).also { length = it }) != -1) {
                            outputStream.write(buf, 0, length)
                        }
            }
        }
    }
}
