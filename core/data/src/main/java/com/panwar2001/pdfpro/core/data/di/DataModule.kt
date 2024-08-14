package com.panwar2001.pdfpro.core.data.di

import com.panwar2001.pdfpro.core.data.repository.AppLocaleRepository
import com.panwar2001.pdfpro.core.data.repository.AppLocaleRepositoryImpl
import com.panwar2001.pdfpro.core.data.repository.Img2PdfRepository
import com.panwar2001.pdfpro.core.data.repository.Img2PdfRepositoryImpl
import com.panwar2001.pdfpro.core.data.repository.Pdf2ImgRepository
import com.panwar2001.pdfpro.core.data.repository.Pdf2ImgRepositoryImpl
import com.panwar2001.pdfpro.core.data.repository.Pdf2TextRepository
import com.panwar2001.pdfpro.core.data.repository.Pdf2TextRepositoryImpl
import com.panwar2001.pdfpro.core.data.repository.ToolsRepository
import com.panwar2001.pdfpro.core.data.repository.ToolsRepositoryImpl
import com.panwar2001.pdfpro.core.data.repository.UserDataRepository
import com.panwar2001.pdfpro.core.data.repository.UserDataRepositoryImpl
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
    abstract fun bindAppLocaleRepository(repository: AppLocaleRepositoryImpl): AppLocaleRepository

    @Singleton
    @Binds
    abstract fun bindImg2PdfRepository(repository: Img2PdfRepositoryImpl ): Img2PdfRepository

    @Singleton
    @Binds
    abstract fun bindPdf2ImgRepository(repository: Pdf2ImgRepositoryImpl): Pdf2ImgRepository

    @Singleton
    @Binds
    abstract fun bindPdf2TxtRepository(repository: Pdf2TextRepositoryImpl): Pdf2TextRepository

    @Singleton
    @Binds
    abstract fun bindToolsRepository(repository: ToolsRepositoryImpl): ToolsRepository

    @Singleton
    @Binds
    abstract fun bindUserDataRepository(repository: UserDataRepositoryImpl): UserDataRepository
}
