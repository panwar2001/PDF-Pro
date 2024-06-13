package com.panwar2001.pdfpro.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import com.panwar2001.pdfpro.R

object DataSource {
    val MenuItems=listOf(
        MenuItem(
            screen = Screens.Home.route,
            title = "Home",
            contentDescription = "Go to home screen",
            icon = R.drawable.home
        ),
        MenuItem(
            screen= Screens.PdfToText.route,
            title = "Pdf to text",
            contentDescription = "Convert pdf to text",
            icon = R.drawable.outline_text
        ),
        MenuItem(
            screen= Screens.PdfToImage.route,
            title = "Pdf to Images",
            contentDescription = "Convert pdf to Images",
            icon = R.drawable.ion_images
        ),
        MenuItem(
            screen= Screens.Img2Pdf.route,
            title = "Images to pdf",
            contentDescription = "Convert Images to pdf",
            icon = R.drawable.pdf_icon
        )

    )


    val FeatureList = listOf(
        ToolsData(R.drawable.outline_text,"Pdf to text",0,Screens.PdfToText.route),
        ToolsData(R.drawable.ion_images,"Pdf to images",1,Screens.PdfToImage.route),
        ToolsData(R.drawable.pdf_icon,"Images to pdf",2,Screens.Img2Pdf.route),
//        ToolsData(R.drawable.compress,"Compress pdf",2),
//        ToolsData(R.drawable.ocr,"Ocr pdf",3),
//        ToolsData(R.drawable.unlock_pdf,"lock pdf",5),
//        ToolsData(R.drawable.summary_icon,"Summarize pdf",6),
//        ToolsData(R.drawable.search_icon,"Searchable pdf",7),
//        ToolsData(R.drawable.msword_icon,"Word to pdf",8)
    )

    val OnBoardList= listOf(
        OnBoardData(icon=R.drawable.ocr,title="Ocr PDF", description = "Unlock the potential of Optical Character Recognition for streamlined text extraction."),
        OnBoardData(icon=R.drawable.pdf_compresser,title="Compress PDF", description = "Effortlessly compress PDF files while preserving quality and reducing file size with our intuitive compression tool"),
        OnBoardData(icon=R.drawable.lock_pdf,title="Lock PDF", description = "Secure your PDF with password protection to safeguard sensitive information.")
    )
    fun getToolData(id:Int): Tool{
        return when(id){
            0-> Tool("Our advanced text extraction algorithm ensures high accuracy, preserving formatting and layout faithfully.","Select PDF file")
            1 -> Tool("Our cutting-edge PDF to images converter ensures high-quality image output, maintaining the integrity and clarity of your document.","Select PDF file")
            2 -> Tool("Our cutting-edge images to PDF converter ensures high-quality PDF, maintaining the integrity and clarity of your document.","Select Images")

            else-> Tool("","upload")
        }
    }

}