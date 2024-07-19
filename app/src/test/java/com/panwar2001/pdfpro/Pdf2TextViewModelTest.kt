package com.panwar2001.pdfpro

import android.net.Uri
import com.panwar2001.pdfpro.view_models.PdfToTextViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.Base64

internal class Pdf2txtViewModelTest {

    private lateinit var pdf2txtViewModel:PdfToTextViewModel
    private lateinit var toolsRepository: FakeToolsRepository

    @Before
    fun setUpViewModel() {
//        toolsRepository= FakeToolsRepository()
//        pdf2txtViewModel= PdfToTextViewModel(toolsRepository)
    }
//    @Test
//    fun testGenerateThumbnailFromPdf(){
//        pdf2txtViewModel.setUri(Uri.EMPTY)
////        pdf2txtViewModel.generateThumbnailFromPDF()
//        assertEquals(pdf2txtViewModel.uiState.value.state,"")
//    }
//    @Test
//    fun testConvertToText(){
//    }

}