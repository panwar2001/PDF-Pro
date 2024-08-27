package com.panwar2001.pdfpro.core.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.DocumentsContract
import androidx.core.content.FileProvider
import com.panwar2001.pdfpro.core.data.model.asEntity
import com.panwar2001.pdfpro.core.database.dao.TextFileDao
import com.panwar2001.pdfpro.core.database.entities.asExternalModel
import com.panwar2001.pdfpro.model.PdfToTextUiState
import com.panwar2001.pdfpro.model.TextFileData
import com.panwar2001.pdfpro.model.TextFileInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton



class Pdf2TextRepositoryImpl @Inject
constructor(@ApplicationContext private val context: Context,
            private val textFileDao: TextFileDao,
            private val toolsRepository: ToolsRepository
): Pdf2TextRepository {
    override suspend fun modifyName(id: Long, name: String) {
        val directory = File(context.filesDir, "TEXT_FILES_DIR")
        val path = textFileDao.getFilePath(id)
        val oldFile = File(path)
        val newFile = toRenameCreateFileWithUniqueName(directory, name)
        if (!oldFile.renameTo(newFile)) {
            throw Exception("Failed to rename file")
        } else {
            textFileDao.updatePath(id, newFile.path)
            DocumentsContract.deleteDocument(context.contentResolver, Uri.EMPTY)
        }
    }
    private  fun toRenameCreateFileWithUniqueName(directory: File, name: String): File {
        var count = 0
        var file = File(directory, name)
        while (file.exists()) {
            count++
            file = File(directory, "${name}_$count.txt")
        }
        return if(count<=1){
            file
        }else{
            File(directory, "${name}_${count-1}.txt")
        }
    }

    override fun initPdfToTextUiState(): PdfToTextUiState {
        return PdfToTextUiState(
            uri = Uri.EMPTY,
            isLoading = false,
            thumbnail = getDefaultThumbnail(),
            pdfFileName = "file.pdf",
            textFileName = "file.txt",
            text = "",
            numPages = 0,
            fileUniqueId = -1
        )
    }

    private fun getDefaultThumbnail(): Bitmap {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }

    override suspend fun createTextFile(
        text: String,
        fileName: String
    ) = withContext(Dispatchers.IO) {
        val directory = File(context.filesDir, "TEXT_FILES_DIR")
        // Create or get the directory
        if (!directory.exists()) {
            directory.mkdir()
        }
        val newFile = createFileWithUniqueName(directory, fileName)
        // Write the content to the file
        FileOutputStream(newFile).use { outputStream ->
            outputStream.write(text.toByteArray())
        }
        val data= TextFileData(id=0, filePath = newFile.absolutePath)
        Pair(
            textFileDao.insertPath(data.asEntity()),
            newFile.name.substringBeforeLast('.')
        )
    }

    private fun createFileWithUniqueName(directory: File, fileName: String): File {
        val name = fileName.substringBeforeLast(".pdf")
        var file = File(directory, "$name.txt")
        var count = 0
        while (file.exists()) {
            count++
            file = File(directory, "${name}_$count.txt")
        }
        return file
    }

    override suspend fun deleteTextFile(id: Long) {
        withContext(Dispatchers.IO) {
            val path = textFileDao.getFilePath(id)
            File(path).delete()
            textFileDao.deletePath(id)
        }
    }

    override suspend fun getTextAndNameFromFile(id: Long): Pair<String, String> {
        return withContext(Dispatchers.IO) {
            val file = File(textFileDao.getFilePath(id))
            Pair(file.readText(), file.name.substringBeforeLast('.'))
        }
    }

    override fun getAllTextFiles(): Flow<List<TextFileInfo>> {
        return textFileDao.getAllFilePaths()
            .map { filePath ->
                withContext(Dispatchers.IO) {
                    filePath.map {
                        val file = File(it.asExternalModel().filePath)
                        TextFileInfo(
                            fileName = file.name,
                            id = it.id,
                            uri = FileProvider.getUriForFile(
                                context,
                                context.packageName + ".fileprovider",
                                file
                            ),
                            fileSize = toolsRepository.getFileSize(file.length())
                        )
                    }
                }
            }
    }

}