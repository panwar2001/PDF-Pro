package com.panwar2001.pdfpro.di

import android.app.Application
import android.content.Context
import com.panwar2001.pdfpro.HiltApplicationClass
import com.panwar2001.pdfpro.HiltApplicationClass_GeneratedInjector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class) // Use ApplicationComponent for Application-scoped bindings
object AppModule {
    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

}
