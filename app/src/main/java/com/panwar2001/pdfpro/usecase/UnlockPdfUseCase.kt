package com.panwar2001.pdfpro.usecase

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.tom_roush.pdfbox.pdmodel.PDDocument
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class UnlockPdfUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(uri:Uri,password:String): Uri= withContext(Dispatchers.Default){
            context.contentResolver.openInputStream(uri).use {
                if(it==null)throw NullPointerException("file descriptor is null")
                val doc = PDDocument.load(it,password)
                if (doc.isEncrypted) {
                    doc.isAllSecurityToBeRemoved = true
                }
                val file = File(context.filesDir, "fs.pdf")
                doc.save(file)
                return@withContext FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileprovider",
                    file
                )
            }
    }
}