package com.panwar2001.pdfpro.pdf2TextTest

import android.content.Context
import android.net.Uri
import android.util.Log

import com.panwar2001.pdfpro.data.Pdf2TextInterface
import com.panwar2001.pdfpro.data.ToolsInterface
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
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.internal.wait
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class Pdf2txtViewModelTest {
    private lateinit var toolsRepository: ToolsInterface
    private lateinit var pdf2textRepository: Pdf2TextInterface
    @MockK
    lateinit var context: Context
    @MockK
    private lateinit var isPdfLockedUseCase: IsPdfLockedUseCase
    @MockK
    private lateinit var uiEventUseCase: UiEventUseCase
    @MockK
    private lateinit var isPdfCorruptedUseCase: IsPdfCorruptedUseCase
    @MockK
    private lateinit var getPdfThumbnailUseCase: GetPdfThumbnailUseCase
    @MockK
    private lateinit var getPdfPageCountUseCase: GetPdfPageCountUseCase
    @MockK
    private lateinit var getFileNameUseCase: GetFileNameUseCase
    @MockK
    private lateinit var unlockPdfUseCase: UnlockPdfUseCase
    @MockK
    private lateinit var convertToTextUseCase: ConvertToTextUseCase

    private lateinit var pdf2txtViewModel:PdfToTextViewModel
    private val uiEventFlow: MutableSharedFlow<EventType> = MutableSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {

        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)
        context= mockk()
        uiEventUseCase= mockk()
        pdf2textRepository=PdfToTextFakeRepository()
        toolsRepository= ToolsFakeRepository(mockk())
        isPdfLockedUseCase= mockk(relaxed = true)
        isPdfLockedUseCase=mockk(relaxed = true)
        isPdfCorruptedUseCase= mockk(relaxed = true)
        getPdfThumbnailUseCase= mockk()
        getPdfPageCountUseCase= mockk(relaxed = true)
        getFileNameUseCase= mockk(relaxed = true)
        unlockPdfUseCase= mockk(relaxed = true)
        convertToTextUseCase= mockk(relaxed = true)

        every { uiEventUseCase.uiEventFlow} returns uiEventFlow.asSharedFlow()
        coEvery { uiEventUseCase(any()) } coAnswers  {
            val event= invocation.args[0] as EventType
            uiEventFlow.emit(event)
        }
        coEvery { getPdfThumbnailUseCase(any()) } coAnswers  {
            val uri= invocation.args[0] as Uri
            val initUri=pdf2textRepository.initPdfToTextUiState().uri
            
            if(uri== pdf2textRepository.initPdfToTextUiState().uri){
                throw Exception("exception pdf thumbnail")
            }
            mockk()
        }


        pdf2txtViewModel= PdfToTextViewModel(
            uiEventUseCase = uiEventUseCase,
            pdf2TextRepository = pdf2textRepository,
            isPdfCorruptedUseCase = isPdfCorruptedUseCase,
            isPdfLockedUseCase = isPdfLockedUseCase,
            getFileNameUseCase = getFileNameUseCase,
            getPdfThumbnailUseCase = getPdfThumbnailUseCase,
            getPdfPageCountUseCase = getPdfPageCountUseCase,
            unlockPdfUseCase = unlockPdfUseCase,
            convertToTextUseCase = convertToTextUseCase,
            toolsRepository = toolsRepository
        )

    }


    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }
    @Test
    fun `test exception thrown in pick pdf function`()= runTest{
        assertFailsWith<Exception> {
            getPdfThumbnailUseCase(pdf2txtViewModel.uiState.value.uri)
        }
    }
    @Test
    fun `test delete file triggers error event on exception`()= runTest{
    var triggered=false
    val job= launch(){
        pdf2txtViewModel.uiEventFlow.collect {
                triggered= (it == EventType.Error)
        }
    }
    pdf2txtViewModel.deleteFile(0L)
    advanceUntilIdle()
    assertTrue(triggered)
    job.cancel()

    assertFailsWith<Exception> {
        pdf2textRepository.deleteTextFile(0L)
    }
}

    @Test
    fun `test delete file does not triggers event when exception not occurs`()= runTest{
        var triggered=false
        val job= launch(){
            pdf2txtViewModel.uiEventFlow.collect {
                triggered= (it == EventType.Error)
            }
        }
        pdf2txtViewModel.deleteFile(1L)

        advanceUntilIdle()
        assertFalse(triggered)
        job.cancel()
    }
}