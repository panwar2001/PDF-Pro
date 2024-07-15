package com.panwar2001.pdfpro.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImagesFolderDao{

  @Query("select * from folders")
  suspend fun getAllFolders(): List<Folder>

  @Insert
  suspend fun createFolder(folder: Folder)

  @Query("delete from folders where id = :folderId")
  suspend fun deleteFolderById(folderId: String)
}