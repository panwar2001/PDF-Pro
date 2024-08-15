package com.panwar2001.pdfpro.screens

sealed class Screens(val route : String) {
    data object OnBoard : Screens("onboard")
    data object FilePicker: Screens("pick")
    data object Settings: Screens("settings"){
        data object SettingsScreen: Screens("settings_screen")
        data object LanguagePickerScreen: Screens("language_picker")
    }
    data object Home: Screens("home") {
        data object HomeScreen: Screens("home_screen")
        data object PdfViewer: Screens("pdfViewer")
    }

    data object PdfToText: Screens("Pdf2Txt") {
        data object PreviewFile : Screens("Pdf2txt_Preview")
        data object TextScreen: Screens("textScreen")
        data object PdfViewer: Screens("pdf2txt_pdfViewer")
        data object TextFilesScreen: Screens("textFiles_screen")
    }
    data object PdfToImage: Screens("Pdf2Img") {
        data object PreviewFile : Screens("Pdf2img_Preview")
        data object PdfViewer: Screens("pdf2img_pdfViewer")
        data object ImageScreen: Screens("imageScreen")
    }
    data object  Img2Pdf: Screens("Img2Pdf"){
        data object PdfViewer: Screens("img2pdf_pdfViewer")
        data object ImagesViewScreen: Screens("images")
        data object  ReorderScreen: Screens("reorder")
        data object SavePdfScreen: Screens("save_pdf")
    }
}
