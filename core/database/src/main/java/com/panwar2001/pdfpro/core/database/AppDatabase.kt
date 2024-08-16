package com.panwar2001.pdfpro.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.panwar2001.pdfpro.core.database.dao.ImagesFolderDao
import com.panwar2001.pdfpro.core.database.dao.TextFileDao
import com.panwar2001.pdfpro.core.database.entities.ImagesFolderEntity
import com.panwar2001.pdfpro.core.database.entities.TextFileEntity

/**
 *  exportSchema should be true in production databases.
 */
@Database(entities = [TextFileEntity::class, ImagesFolderEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imgFolderDao(): ImagesFolderDao
    abstract fun textFileDao(): TextFileDao
}