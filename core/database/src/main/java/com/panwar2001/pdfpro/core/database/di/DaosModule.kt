package com.panwar2001.pdfpro.core.database.di

import com.panwar2001.pdfpro.core.database.AppDatabase
import com.panwar2001.pdfpro.core.database.dao.ImagesFolderDao
import com.panwar2001.pdfpro.core.database.dao.TextFileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun provideTextFileDao(database: AppDatabase): TextFileDao = database.textFileDao()

    @Provides
    fun provideImgFolderDao(database: AppDatabase): ImagesFolderDao = database.imgFolderDao()
}
