package com.panwar2001.pdfpro.core.data.model

import com.panwar2001.pdfpro.core.database.entities.TextFileEntity
import com.panwar2001.pdfpro.model.TextFileData

fun TextFileData.asEntity() = TextFileEntity(
    filePath = filePath
)