plugins {
    alias(libs.plugins.pdfpro.android.library)
    alias(libs.plugins.pdfpro.android.library.compose)
}

android {
    namespace = "com.panwar2001.pdfpro.model"
}
dependencies{
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
}