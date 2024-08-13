package com.panwar2001.pdfpro.core.database.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey val id: String,
    val lastModified: Long
)
