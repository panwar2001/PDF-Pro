plugins {
    alias(libs.plugins.pdfpro.android.library)
    alias(libs.plugins.pdfpro.android.library.compose)
    alias(libs.plugins.pdfpro.hilt)
}

android {
    namespace = "com.panwar2001.pdfpro.pdf2txt.pickpdf"
}

dependencies {
    implementation(projects.core.ui)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}