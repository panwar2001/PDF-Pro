package com.panwar2001.pdfpro.core.data.di

import android.app.Application
import android.content.Context
import com.panwar2001.pdfpro.core.data.repository.ToolsRepository
import com.panwar2001.pdfpro.core.data.repository.ToolsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class) // Use ApplicationComponent for Application-scoped bindings
object ContextProviderModule {
    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

}

