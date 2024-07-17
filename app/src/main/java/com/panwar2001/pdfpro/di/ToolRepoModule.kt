package com.panwar2001.pdfpro.di

import com.panwar2001.pdfpro.data.ToolsInterfaceRepository
import com.panwar2001.pdfpro.data.ToolsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract  class  ToolRepoModule {

    @Singleton
    @Binds
    abstract fun bindToolsRepository(repository: ToolsRepository):ToolsInterfaceRepository
}