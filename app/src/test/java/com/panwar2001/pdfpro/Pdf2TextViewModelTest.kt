package com.panwar2001.pdfpro

import android.graphics.Bitmap
import android.net.Uri
import com.panwar2001.pdfpro.data.Pdf2TextRepository
import com.panwar2001.pdfpro.data.ToolsInterfaceRepository
import com.panwar2001.pdfpro.data.ToolsRepository
import com.panwar2001.pdfpro.view_models.PdfToTextUiState
import com.panwar2001.pdfpro.view_models.PdfToTextViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotSame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class Pdf2txtViewModelTest {

    private lateinit var pdf2txtViewModel:PdfToTextViewModel
    @Mock
    private lateinit var toolsRepository: ToolsRepository
    @Mock
    private lateinit var pdf2textRepository: Pdf2TextRepository


    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(pdf2textRepository.initPdfToTextUiState()).thenReturn(
            PdfToTextUiState(
                uri=mock(Uri::class.java),
                isLoading=false,
                thumbnail=mock(Bitmap::class.java),
                fileName="file.pdf",
                text= "",
                numPages=0,
                userMessage=0,
                state="",
                fileUniqueId = -1)
        )
        pdf2txtViewModel= PdfToTextViewModel(toolsRepository,pdf2textRepository)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }
    @Test
    fun testGenerateThumbnailFromPdf()= runTest{
        val uri= mock(Uri::class.java)
        Mockito.`when`(toolsRepository.getThumbnailOfPdf(any())).thenAnswer{ invocation->
            val t=invocation.getArgument(0) as Uri
            if(t==uri){
                throw Exception("Exception check")
            }
            return@thenAnswer mock(Bitmap::class.java)
        }
        Mockito.`when`(toolsRepository.getNumPages(any())).thenReturn(2)
        Mockito.`when`(toolsRepository.getPdfName(any())).thenReturn("name.pdf")

        pdf2txtViewModel.setUri(uri)
        pdf2txtViewModel.generateThumbnailFromPDF()
        advanceUntilIdle()// Ensures all pending coroutines are executed
        assertEquals(pdf2txtViewModel.uiState.value.state,"error")
        assertNotSame(pdf2txtViewModel.uiState.value.numPages,2)
        val uri2=mock(Uri::class.java)
        pdf2txtViewModel.setUri(uri2)
        pdf2txtViewModel.generateThumbnailFromPDF()
        advanceUntilIdle()// Ensures all pending coroutines are executed
        assertEquals(pdf2txtViewModel.uiState.value.state,"success")
    }
    @Test
    fun testConvertToText()= runTest{
        val uri= mock(Uri::class.java)
        Mockito.`when`(toolsRepository.convertToText(any())).thenAnswer{ invocation->
            val t=invocation.getArgument(0) as Uri
            if(t==uri){
                throw Exception("Exception check")
            }
            return@thenAnswer "returned text"
        }
        Mockito.`when`(pdf2textRepository.createTextFile(any(),any())).thenReturn(
            Pair(0,"file name")
        )

        pdf2txtViewModel.setUri(uri)
        pdf2txtViewModel.convertToText()
        advanceUntilIdle()// Ensures all pending coroutines are executed
        assertEquals(pdf2txtViewModel.uiState.value.state,"error")

        val uri2=mock(Uri::class.java)
        pdf2txtViewModel.setUri(uri2)
        pdf2txtViewModel.convertToText()
        advanceUntilIdle()// Ensures all pending coroutines are executed
        assertEquals(pdf2txtViewModel.uiState.value.state,"success")
        assertEquals(pdf2txtViewModel.uiState.value.text,"returned text")
    }

}