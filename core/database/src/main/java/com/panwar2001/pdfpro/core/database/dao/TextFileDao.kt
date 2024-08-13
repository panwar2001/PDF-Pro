package com.panwar2001.pdfpro.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.panwar2001.pdfpro.core.database.entities.TextFileEntity
import com.panwar2001.pdfpro.model.TextFileData
import kotlinx.coroutines.flow.Flow

@Dao
interface TextFileDao {

    @Insert
    suspend fun insertPath(path: TextFileEntity): Long

    @Query("delete from textFiles where id=:id")
    suspend fun deletePath(id: Long)

    @Query("select * from textFiles")
    fun getAllFilePaths(): Flow<List<TextFileEntity>>

    @Query("select filePath from textFiles where id=:id")
    suspend fun getFilePath(id:Long): String

    @Query("update textFiles set filePath=:path where id= :id")
    suspend fun updatePath(id:Long, path: String)
}