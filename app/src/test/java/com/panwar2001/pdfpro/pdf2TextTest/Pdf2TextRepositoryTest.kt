package com.panwar2001.pdfpro.pdf2TextTest

import android.content.Context
import com.panwar2001.pdfpro.data.Pdf2TextRepository
import com.panwar2001.pdfpro.data.source.local.TextFile
import com.panwar2001.pdfpro.data.source.local.TextFileDao
import com.panwar2001.pdfpro.core.domain.GetFileSizeUseCase
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import org.junit.Before

class Pdf2TextRepositoryTest {
  private lateinit var  pdf2TextRepository: Pdf2TextRepository
  private lateinit var  context: Context
  private lateinit var textFileDao: TextFileDao
  private lateinit var getFileSizeUseCase: com.panwar2001.pdfpro.core.domain.GetFileSizeUseCase
  @Before
  fun setup(){
      context= mockk()
      textFileDao= Dao()
      getFileSizeUseCase= com.panwar2001.pdfpro.core.domain.GetFileSizeUseCase()
      pdf2TextRepository= Pdf2TextRepository(context,textFileDao,getFileSizeUseCase)
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