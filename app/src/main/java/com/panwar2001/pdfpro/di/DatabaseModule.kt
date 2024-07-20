package com.panwar2001.pdfpro.di

import android.content.Context
import androidx.room.Room
import com.panwar2001.pdfpro.data.source.local.AppDatabase
import com.panwar2001.pdfpro.data.source.local.TextFileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "Tasks.db"
        ).build()
    }

    @Provides
    fun provideTextFileDao(database: AppDatabase): TextFileDao = database.textFileDao()
}
