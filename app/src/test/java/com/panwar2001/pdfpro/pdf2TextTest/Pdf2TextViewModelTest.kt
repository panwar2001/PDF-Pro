package com.panwar2001.pdfpro.pdf2TextTest

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.panwar2001.pdfpro.data.Pdf2TextInterface
import com.panwar2001.pdfpro.data.ToolsRepository
import com.panwar2001.pdfpro.usecase.ConvertToTextUseCase
import com.panwar2001.pdfpro.usecase.EventType
import com.panwar2001.pdfpro.usecase.GetFileNameUseCase
import com.panwar2001.pdfpro.usecase.GetPdfPageCountUseCase
import com.panwar2001.pdfpro.usecase.GetPdfThumbnailUseCase
import com.panwar2001.pdfpro.usecase.IsPdfCorruptedUseCase
import com.panwar2001.pdfpro.usecase.IsPdfLockedUseCase
import com.panwar2001.pdfpro.usecase.UiEventUseCase
import com.panwar2001.pdfpro.usecase.UnlockPdfUseCase
import com.panwar2001.pdfpro.view_models.PdfToTextViewModel
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
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
import org.mockito.kotlin.any
import org.mockito.Mockito
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class Pdf2txtViewModelTest {
    private lateinit var pdf2txtViewModel:PdfToTextViewModel
    @Mock
    private lateinit var toolsRepository: ToolsRepository
    @Mock
    private lateinit var pdf2textRepository: Pdf2TextInterface
    @Mock
    private lateinit var isPdfLockedUseCase: IsPdfLockedUseCase
    @Mock
    private lateinit var uiEventUseCase: UiEventUseCase
    @Mock
    private lateinit var isPdfCorruptedUseCase: IsPdfCorruptedUseCase
    @Mock
    private lateinit var getPdfThumbnailUseCase: GetPdfThumbnailUseCase
    @Mock
    private lateinit var getPdfPageCountUseCase: GetPdfPageCountUseCase
    @Mock
    private lateinit var getFileNameUseCase: GetFileNameUseCase
    @Mock
    private lateinit var unlockPdfUseCase: UnlockPdfUseCase
    @Mock
    private lateinit var convertToTextUseCase: ConvertToTextUseCase


    @Before
    fun setup() {

        Dispatchers.setMain(StandardTestDispatcher())
        MockitoAnnotations.openMocks(this)
        pdf2textRepository=PdfToTextFakeRepository()
//        Mockito.`when`(pdf2textRepository.getAllTextFiles()).thenReturn(null)
        val context= mock(Context::class.java)
        isPdfLockedUseCase= IsPdfLockedUseCase(context)
        uiEventUseCase= UiEventUseCase()
        isPdfLockedUseCase=IsPdfLockedUseCase(context)
        getPdfThumbnailUseCase= GetPdfThumbnailUseCase(context)
        getPdfPageCountUseCase= GetPdfPageCountUseCase(context)
        getFileNameUseCase= GetFileNameUseCase(context)
        unlockPdfUseCase= UnlockPdfUseCase(context)
        convertToTextUseCase= mock(ConvertToTextUseCase::class.java)

        pdf2txtViewModel= PdfToTextViewModel(
            toolsRepository,
            pdf2textRepository,
            isPdfLockedUseCase,
            uiEventUseCase,
            isPdfCorruptedUseCase,
            getPdfThumbnailUseCase,
            getPdfPageCountUseCase,
            getFileNameUseCase,
            unlockPdfUseCase,
            convertToTextUseCase)

    }


    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }
//    @Test
//    fun falt(){
//
//    }
//    @Test
//    fun `test to pick pdf`()= runTest{
//        val uri= pdf2txtViewModel.uiState.value.uri
//        Mockito.`when`(getPdfThumbnailUseCase(any())).thenAnswer{ invocation->
//            val t=invocation.getArgument(0) as Uri
//            if(t==uri){
//                throw Exception("Exception check")
//            }
//            return@thenAnswer mock(Bitmap::class.java)
//        }
//        Mockito.`when`(getPdfPageCountUseCase(any())).thenReturn(2)
//        Mockito.`when`(getFileNameUseCase(any())).thenReturn("name.pdf")
//
//        pdf2txtViewModel.pickPdf(uri)
//        advanceUntilIdle()// Ensures all pending coroutines are executed
//        pdf2txtViewModel.uiEventFlow.collectLatest {
//                assertTrue(it==EventType.Error)
//        }
//        val uri2=mock(Uri::class.java)
//        pdf2txtViewModel.pickPdf(uri2)
//        advanceUntilIdle()// Ensures all pending coroutines are executed
//        pdf2txtViewModel.uiEventFlow.collectLatest {
//            assertTrue(it==EventType.Success)
//        }
//    }
    @Test
    fun testConvertToText()= runTest{
        val uri1 = pdf2txtViewModel.uiState.value.uri
        Mockito.`when`(convertToTextUseCase(any())).thenAnswer{ invocation->
            val t=invocation.getArgument(0) as Uri
            if(t==uri1){
                throw Exception("Exception check")
            }
            return@thenAnswer "returned text"
        }
        Mockito.`when`(pdf2textRepository.createTextFile(anyString(), anyString())).thenReturn(
            Pair(0,"file name")
        )
        val uri2=mock(Uri::class.java)
        pdf2txtViewModel.pickPdf(uri2)
        advanceUntilIdle()// Ensures all pending coroutines are executed
        pdf2txtViewModel.convertToText(uri2,"file.pdf")
        advanceUntilIdle()// Ensures all pending coroutines are executed
        pdf2txtViewModel.uiEventFlow.collectLatest {
            assertTrue(it!=EventType.Error)
        }

        pdf2txtViewModel.pickPdf(uri1)
        advanceUntilIdle()// Ensures all pending coroutines are executed
        pdf2txtViewModel.convertToText(uri1,"file.pdf")
        advanceUntilIdle()// Ensures all pending coroutines are executed
        pdf2txtViewModel.uiEventFlow.collectLatest {
            assertTrue(it==EventType.Error)
        }
    }
//    @Test
//    fun testDeleteFile()= runTest{
//        Mockito.`when`(pdf2textRepository.deleteTextFile(anyLong())).thenAnswer{ invocation->
//            val t=invocation.getArgument(0) as Int
//            if(t==0){
//                throw NullPointerException("error message")
//            }
//        }
//    /**
//     * test delete File when throws an exception
//     */
//        pdf2txtViewModel.deleteFile(0L)
//        assertFalse(pdf2txtViewModel.uiState.value.isLoading)
//
//        /**
//         * test delete File when does not throw an exception
//         */
//        pdf2txtViewModel.deleteFile(1L)
//        assertFalse(pdf2txtViewModel.uiState.value.isLoading)
//    }
}