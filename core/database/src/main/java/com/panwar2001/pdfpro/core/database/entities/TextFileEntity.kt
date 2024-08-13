package com.panwar2001.pdfpro.core.database.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "textFiles")
data class TextFileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long=0,
    val filePath: String
)