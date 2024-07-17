package com.panwar2001.pdfpro.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *  exportSchema should be true in production databases.
 */
@Database(entities = [Folder::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
//    abstract fun imagesDao(): ImagesFolderDao
}