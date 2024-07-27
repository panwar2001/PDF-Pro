package com.panwar2001.pdfpro.di

import com.panwar2001.pdfpro.data.Img2PdfInterface
import com.panwar2001.pdfpro.data.Img2PdfRepository
import com.panwar2001.pdfpro.data.Pdf2ImgInterface
import com.panwar2001.pdfpro.data.Pdf2ImgRepository
import com.panwar2001.pdfpro.data.Pdf2TextInterface
import com.panwar2001.pdfpro.data.Pdf2TextRepository
import com.panwar2001.pdfpro.data.ToolsInterface
import com.panwar2001.pdfpro.data.ToolsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract  class  RepoModule {

    @Singleton
    @Binds
    abstract fun bindToolsRepository(repository: ToolsRepository):ToolsInterface

    @Singleton
    @Binds
    abstract fun bindPdf2TxtRepository(repository: Pdf2TextRepository):Pdf2TextInterface

    @Singleton
    @Binds
    abstract fun bindPdf2ImgRepository(repository: Pdf2ImgRepository):Pdf2ImgInterface

    @Singleton
    @Binds
    abstract fun bindImg2PdfRepository(repository: Img2PdfRepository ):Img2PdfInterface
}