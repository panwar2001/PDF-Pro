package com.panwar2001.pdfpro.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.panwar2001.pdfpro.core.database.dao.TextFileDao

/**
 *  exportSchema should be true in production databases.
 */
@Database(entities = [TextFileDao::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    //    abstract fun imagesDao(): ImagesFolderDao
    abstract  fun textFileDao(): TextFileDao
}