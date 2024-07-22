package com.panwar2001.pdfpro.view_models

import androidx.lifecycle.ViewModel
import com.panwar2001.pdfpro.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OnBoardData(
    val icon: Int,
    val title:Int,
    val description:Int
)
class OnBoardScreenViewModel: ViewModel(){
    val onBoardList= listOf(
        OnBoardData(icon= R.drawable.ocr,
            title= R.string.ocr_pdf,
            description = R.string.ocr_description),
        OnBoardData(icon= R.drawable.pdf_compresser,
            title= R.string.compress_pdf,
            description = R.string.compress_pdf_description),
        OnBoardData(icon= R.drawable.lock_pdf,
            title= R.string.lock_pdf,
            description = R.string.lock_description)
    )
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun setLoading(loading:Boolean){
        _loading.update {loading}
    }
}