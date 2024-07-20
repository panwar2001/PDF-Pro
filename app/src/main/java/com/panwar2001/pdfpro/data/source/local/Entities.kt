package com.panwar2001.pdfpro.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey val id: String,
    val lastModified: Long
)

@Entity(tableName = "textFiles")
data class TextFile(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val filePath: String
)