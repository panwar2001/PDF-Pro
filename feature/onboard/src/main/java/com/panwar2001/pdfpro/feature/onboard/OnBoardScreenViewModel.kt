package com.panwar2001.pdfpro.feature.onboard

import android.content.Context
import androidx.lifecycle.ViewModel
import com.panwar2001.pdfpro.onboard.R
import com.panwar2001.pdfpro.screens.Screens

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

    /**
     * If onboarding is done once when app is run for first time then always
     * home screen is opened after splash screen.
     */

    private fun setOnboardingFinished(){
        this.getSharedPreferences("onBoarding", MODE_PRIVATE)
            .edit().putBoolean("isFinished", true).apply()
    }
}