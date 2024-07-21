package com.panwar2001.pdfpro.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TextFileDao {
    @Insert
    suspend fun insertPath(path: TextFile): Long

    @Query("delete from textFiles where id=:id")
    suspend fun deletePath(id: Long)

    @Query("select * from textFiles")
    fun getAllFilePaths(): Flow<List<TextFile>>

    @Query("select filePath from textFiles where id=:id")
    suspend fun getFilePath(id:Long): String

    @Query("update textFiles set filePath=:path where id= :id")
    suspend fun updatePath(id:Long, path: String)
}