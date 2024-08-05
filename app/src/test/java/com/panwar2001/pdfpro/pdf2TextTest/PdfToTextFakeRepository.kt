package com.panwar2001.pdfpro.pdf2TextTest

import android.graphics.Bitmap
import android.net.Uri
import com.panwar2001.pdfpro.data.Pdf2TextInterface
import com.panwar2001.pdfpro.data.TextFileInfo
import com.panwar2001.pdfpro.view_models.PdfToTextUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import org.mockito.Mockito.mock

class PdfToTextFakeRepository :Pdf2TextInterface {
    private var textFiles= mutableListOf<TextFileInfo>()
    init {
        repeat(5) {
            textFiles.add(TextFileInfo("", it.toLong(), mock(Uri::class.java), "1 mb"))
        }
    }
    override fun initPdfToTextUiState(): PdfToTextUiState {
        return PdfToTextUiState(
            uri=mock(Uri::class.java),
            isLoading=false,
            thumbnail= mock(Bitmap::class.java),
            pdfFileName="file.pdf",
            textFileName = "",
            text= "",
            numPages=0,
            fileUniqueId = 0)
    }

    override suspend fun createTextFile(text: String, fileName: String): Pair<Long, String> {
        return Pair(0,"")
    }

    override suspend fun deleteTextFile(id: Long) {
        var index=-1
        for(i in 0..<textFiles.size){
            if(textFiles[i].id==id){
                index=i
            }
        }
        if(index!=-1) {
            textFiles.removeAt(index)
        }
    }

    override fun getAllTextFiles(): Flow<List<TextFileInfo>> = flow {
        emit(textFiles)
    }

    override suspend fun getTextAndNameFromFile(id: Long): Pair<String, String> {
        return Pair("text "," file name.txt")
    }

    override suspend fun modifyName(id: Long, name: String) {

    }
}