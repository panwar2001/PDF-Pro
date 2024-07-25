package com.panwar2001.pdfpro.pdf2TextTest

import android.graphics.Bitmap
import android.net.Uri
import com.panwar2001.pdfpro.data.Pdf2TextRepository
import com.panwar2001.pdfpro.data.ToolsRepository
import com.panwar2001.pdfpro.view_models.PdfToTextUiState
import com.panwar2001.pdfpro.view_models.PdfToTextViewModel
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class Pdf2txtViewModelTest {
    private lateinit var pdf2txtViewModel:PdfToTextViewModel

    @Mock
    private lateinit var toolsRepository: ToolsRepository
    @Mock
    private lateinit var pdf2textRepository: Pdf2TextRepository

    private lateinit var defaultUri: Uri


    @Before
    fun setup() {

        Dispatchers.setMain(StandardTestDispatcher())
        defaultUri=mock(Uri::class.java)

        MockitoAnnotations.openMocks(this)
        Mockito.`when`(pdf2textRepository.initPdfToTextUiState()).thenReturn(
            PdfToTextUiState(
                uri=defaultUri,
                isLoading=false,
                thumbnail=mock(Bitmap::class.java),
                pdfFileName="file.pdf",
                textFileName = "",
                text= "",
                numPages=0,
                userMessage=0,
                fileUniqueId = 0,
                triggerSuccess = false,
                isError = false)
        )
        Mockito.`when`(pdf2textRepository.getAllTextFiles()).thenReturn(null)
        pdf2txtViewModel= PdfToTextViewModel(toolsRepository,pdf2textRepository)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }
    @Test
    fun testGenerateThumbnailFromPdf()= runTest{
        Mockito.`when`(toolsRepository.getThumbnailOfPdf(any())).thenAnswer{ invocation->
            val t=invocation.getArgument(0) as Uri
            if(t==defaultUri){
                throw Exception("Exception check")
            }
            return@thenAnswer mock(Bitmap::class.java)
        }
        Mockito.`when`(toolsRepository.getNumPages(any())).thenReturn(2)
        Mockito.`when`(toolsRepository.getPdfName(any())).thenReturn("name.pdf")

        pdf2txtViewModel.pickPdf(defaultUri)
        advanceUntilIdle()// Ensures all pending coroutines are executed
        assertTrue(pdf2txtViewModel.uiState.value.isError)

        val uri2=mock(Uri::class.java)
        pdf2txtViewModel.pickPdf(uri2)
        advanceUntilIdle()// Ensures all pending coroutines are executed
        assertTrue(pdf2txtViewModel.uiState.value.triggerSuccess)
    }
    @Test
    fun testConvertToText()= runTest{
        Mockito.`when`(toolsRepository.convertToText(any())).thenAnswer{ invocation->
            val t=invocation.getArgument(0) as Uri
            if(t==defaultUri){
                throw Exception("Exception check")
            }
            return@thenAnswer "returned text"
        }
        Mockito.`when`(pdf2textRepository.createTextFile(any(),any())).thenReturn(
            Pair(0,"file name")
        )
        Mockito.`when`(toolsRepository.getDefaultThumbnail()).thenReturn(mock(Bitmap::class.java))
        Mockito.`when`(toolsRepository.getThumbnailOfPdf(any())).thenReturn(mock(Bitmap::class.java))
        Mockito.`when`(toolsRepository.getNumPages(any())).thenReturn(2)
        Mockito.`when`(toolsRepository.getPdfName(any())).thenReturn("name.pdf")

        val uri=mock(Uri::class.java)
        pdf2txtViewModel.pickPdf(uri)
        advanceUntilIdle()// Ensures all pending coroutines are executed
        pdf2txtViewModel.convertToText()
        advanceUntilIdle()// Ensures all pending coroutines are executed
        assertFalse(pdf2txtViewModel.uiState.value.isError)


        pdf2txtViewModel.pickPdf(defaultUri)
        advanceUntilIdle()// Ensures all pending coroutines are executed
        pdf2txtViewModel.convertToText()
        advanceUntilIdle()// Ensures all pending coroutines are executed
        assertTrue(pdf2txtViewModel.uiState.value.isError)
}
    @Test
    fun testDeleteFile()= runTest{
        Mockito.`when`(pdf2textRepository.deleteTextFile(any())).thenAnswer{ invocation->
            val t=invocation.getArgument(0) as Int
            if(t==0){
                throw NullPointerException("error message")
            }
        }
    /**
     * test delete File when throws an exception
     */
        pdf2txtViewModel.deleteFile(0)
        assertFalse(pdf2txtViewModel.uiState.value.isLoading)

        /**
         * test delete File when does not throw an exception
         */
        pdf2txtViewModel.deleteFile(1)
        assertFalse(pdf2txtViewModel.uiState.value.isLoading)
    }
}