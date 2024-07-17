package com.panwar2001.pdfpro.data

import androidx.compose.ui.graphics.Color
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.navigation.Screens

object DataSource {
    val ToolsList = listOf(
        ToolsData(iconId=R.drawable.outline_text,
                  title = R.string.pdf2text,
                  route= Screens.PdfToText.route,
                  iconColor= Color(0xff54e1b6)
        ),
        ToolsData(R.drawable.ion_images,
                  title= R.string.pdf2img,
                  route= Screens.PdfToImage.route,
                  iconColor = Color(0xffaeb4fd)),
        ToolsData(R.drawable.pdf_icon,
                  title=R.string.img2pdf,
                  route = Screens.Img2Pdf.route,
                  iconColor = Color(0xfffdbda1)),
//        ToolsData(R.drawable.compress,"Compress pdf",2),
//        ToolsData(R.drawable.ocr,"Ocr pdf",3),
//        ToolsData(R.drawable.unlock_pdf,"lock pdf",5),
//        ToolsData(R.drawable.summary_icon,"Summarize pdf",6),
//        ToolsData(R.drawable.search_icon,"Searchable pdf",7),
//        ToolsData(R.drawable.msword_icon,"Word to pdf",8)
    )

    fun getToolData(id:Int): Tool{
        return when(id){
            R.string.pdf2text-> Tool(R.string.pdf2txt_description,R.string.select_pdf)
            R.string.pdf2img -> Tool(R.string.pdf2img_description,R.string.select_pdf)
            R.string.img2pdf-> Tool(R.string.img2pdf_description, R.string.select_images)

            else-> Tool(0,0)
        }
    }

}