package com.panwar2001.pdfpro.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.panwar2001.pdfpro.model.TextFileData

@Entity(tableName = "textFiles")
data class TextFileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long=0,
    val filePath: String
)

fun TextFileEntity.asExternalModel() = TextFileData(
    id = id,
    filePath = filePath
)