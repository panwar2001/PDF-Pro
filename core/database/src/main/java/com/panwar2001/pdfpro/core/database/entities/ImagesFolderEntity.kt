package com.panwar2001.pdfpro.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
data class ImagesFolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val albumName: String,
    val date: Long
)