package com.panwar2001.pdfpro.data.source.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TextFileDao {
    @Query("insert into textFiles(filePath) values(:path)")
    suspend fun insertPath(path:String)

    @Query("delete from textFiles where id=:id")
    suspend fun deletePath(id: Long)

    @Query("select * from textFiles")
    fun getAllFilePaths(): Flow<List<TextFile>>

    @Query("select filePath from textFiles where id=:id")
    suspend fun getFilePath(id:Long): String
}