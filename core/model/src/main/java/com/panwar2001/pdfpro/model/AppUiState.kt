package com.panwar2001.pdfpro.model

import android.net.Uri

data class AppUiState(
    val text:String,
    val query:String,
    val isAscending: Boolean,
    val sortOption:Int,
    val searchBarActive:Boolean,
    val isBottomSheetVisible:Boolean,
    val pdfUri: Uri,
    val pdfName:String,
    val numPages:Int)

