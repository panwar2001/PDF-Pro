package com.panwar2001.pdfpro.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.panwar2001.pdfpro.core.database.entities.ImagesFolderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesFolderDao {
    @Insert
    suspend fun createImgFolder(album : ImagesFolderEntity): Long

    @Query("select * from folders")
    fun retrieveAllImgFolders(): Flow<List<ImagesFolderEntity>>

    @Query("delete from folders where id=:id")
    suspend fun deleteFolder(id:String)
}
