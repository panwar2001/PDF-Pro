package com.panwar2001.pdfpro.pdf2TextTest

import android.content.Context
import com.panwar2001.pdfpro.data.Pdf2TextRepository
import com.panwar2001.pdfpro.data.getFileSize
import com.panwar2001.pdfpro.data.source.local.TextFile
import com.panwar2001.pdfpro.data.source.local.TextFileDao
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import org.junit.Before
import org.junit.Test

class Pdf2TextRepositoryTest {
  private lateinit var  pdf2TextRepository: Pdf2TextRepository
  private lateinit var  context: Context
  private lateinit var textFileDao: TextFileDao
  @Before
  fun setup(){
      context= mockk()
      textFileDao= Dao()
      pdf2TextRepository= Pdf2TextRepository(context,textFileDao)
  }
    @Test
    fun `get file size test , the input is in bytes and the output should be adjusted to bytes, kb and mb `(){
        assertEquals(getFileSize(100),"100 Byte")
        assertEquals(getFileSize(1024),"1.0 KB")
        assertEquals(getFileSize(512*3),"1.5 KB")
        assertEquals(getFileSize(1024*1024),"1.0 MB")
        assertEquals(getFileSize(1024*10240),"10.0 MB")
    }

}
class Dao: TextFileDao{
    override suspend fun insertPath(path: TextFile): Long {
     return 0
    }

    override suspend fun deletePath(id: Long) {
    }

    override fun getAllFilePaths(): Flow<List<TextFile>> {
        return mockk()
    }

    override suspend fun getFilePath(id: Long): String {
        return ""
    }

    override suspend fun updatePath(id: Long, path: String) {

    }
}