package com.panwar2001.pdfpro.feature.onboard

import androidx.lifecycle.ViewModel
import com.panwar2001.pdfpro.onboard.R

data class OnBoardData(
    val icon: Int,
    val title:Int,
    val description:Int
)
class OnBoardScreenViewModel: ViewModel(){
    val onBoardList= listOf(
        OnBoardData(icon= R.drawable.ocr,
            title= R.string.pdf2text,
            description = R.string.pdf2txt_description),
        OnBoardData(icon= R.drawable.pdf_svg,
            title= R.string.img2pdf,
            description = R.string.img2pdf_description)
    )
}