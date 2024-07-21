package com.panwar2001.pdfpro

import android.content.Context
import com.panwar2001.pdfpro.data.Pdf2TextRepository
import com.panwar2001.pdfpro.data.source.local.TextFile
import com.panwar2001.pdfpro.data.source.local.TextFileDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class Pdf2TextRepositoryTest {
  private lateinit var  pdf2TextRepository: Pdf2TextRepository
  private lateinit var  context: Context
  private lateinit var textFileDao: TextFileDao
  @Before
  fun setup(){
      context= mock(Context::class.java)
      MockitoAnnotations.openMocks(this)
      textFileDao= Dao()
      pdf2TextRepository= Pdf2TextRepository(context,textFileDao)
  }
    @Test
    fun getFileSizeTest(){
        assertEquals(pdf2TextRepository.getFileSize(100),"100 Byte")
        assertEquals(pdf2TextRepository.getFileSize(1024),"1.0 KB")
        assertEquals(pdf2TextRepository.getFileSize(512*3),"1.5 KB")
        assertEquals(pdf2TextRepository.getFileSize(1024*1024),"1.0 MB")
        assertEquals(pdf2TextRepository.getFileSize(1024*10240),"10.0 MB")
    }
}
class Dao: TextFileDao{
    override suspend fun insertPath(path: TextFile): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deletePath(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getAllFilePaths(): Flow<List<TextFile>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFilePath(id: Long): String {
        TODO("Not yet implemented")
    }

    override suspend fun updatePath(id: Long, path: String) {
        TODO("Not yet implemented")
    }

}