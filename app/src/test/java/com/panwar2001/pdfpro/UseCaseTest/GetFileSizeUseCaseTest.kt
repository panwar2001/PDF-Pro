package com.panwar2001.pdfpro.UseCaseTest

import com.panwar2001.pdfpro.core.domain.GetFileSizeUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class GetFileSizeUseCaseTest {
    private lateinit var getFileSizeUseCase: com.panwar2001.pdfpro.core.domain.GetFileSizeUseCase

    @Before
    fun setup() {
        getFileSizeUseCase = com.panwar2001.pdfpro.core.domain.GetFileSizeUseCase()
    }

    @Test
    fun `get file size test , the input is in bytes and the output should be adjusted to bytes, kb and mb `() {
        assertEquals(getFileSizeUseCase(null), "0 Byte")
        assertEquals(getFileSizeUseCase(100), "100 Byte")
        assertEquals(getFileSizeUseCase(1024), "1.0 KB")
        assertEquals(getFileSizeUseCase(512 * 3), "1.5 KB")
        assertEquals(getFileSizeUseCase(1024 * 1024), "1.0 MB")
        assertEquals(getFileSizeUseCase(1024 * 10240), "10.0 MB")
    }
}
