package com.panwar2001.pdfpro.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.panwar2001.pdfpro.data.source.local.TextFileDao
import com.panwar2001.pdfpro.view_models.PdfToTextUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

interface Pdf2TextInterface {
    fun initPdfToTextUiState(): PdfToTextUiState

    suspend fun createTextFile(text:String,fileName: String)

    suspend fun deleteTextFile(id: Long)

    fun getAllTextFiles(): Flow<List<TextFileInfo>>
}

@Singleton
class Pdf2TextRepository @Inject
constructor(@ApplicationContext private val context: Context,
            private val textFileDao: TextFileDao):Pdf2TextInterface{

    override fun initPdfToTextUiState(): PdfToTextUiState {
        return PdfToTextUiState(
            uri=Uri.EMPTY,
            isLoading=false,
            thumbnail=getDefaultThumbnail(),
            fileName="file.pdf",
            text= "",
            numPages=0,
            userMessage=0,
            state=""
        )
    }

    private fun getDefaultThumbnail(): Bitmap {
        return Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888)
    }

    override  suspend fun createTextFile(text: String,fileName: String) {
        val directory = File(context.filesDir, "TEXT_FILES_DIR")
        withContext(Dispatchers.IO) {
            // Create or get the directory
            if (!directory.exists()) {
                directory.mkdir()
            }
            val newFile = createFileWithUniqueName(directory, fileName)
            // Write the content to the file
            FileOutputStream(newFile).use { outputStream ->
                outputStream.write(text.toByteArray())
            }
            textFileDao.insertPath(newFile.absolutePath)
        }
    }
   private fun createFileWithUniqueName(directory: File, fileName: String): File {
        val name=fileName.substringBeforeLast('.')
        var file = File(directory, "$name.txt")
        var count = 0
        while (file.exists()) {
            count++
            file = File(directory, "${name}_$count.txt")
        }
        return file
    }
    override suspend fun deleteTextFile(id: Long) {
        withContext(Dispatchers.IO){
            val path = textFileDao.getFilePath(id)
            File(path).delete()
            textFileDao.deletePath(id)
        }
    }


     override fun getAllTextFiles(): Flow<List<TextFileInfo>> {
        return textFileDao.getAllFilePaths()
            .map { filePath ->
                withContext(Dispatchers.IO) {
                    filePath.map{
                        val file = File(it.filePath)
                        TextFileInfo(
                            fileName = file.name,
                            id = it.id,
                            uri = file.toUri()
                        )
                    }
                }
            }
          }
       }


data class TextFileInfo(
    val fileName: String,
    val id: Long,
    val uri: Uri
)