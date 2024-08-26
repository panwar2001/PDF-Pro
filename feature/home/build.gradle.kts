plugins {
    alias(libs.plugins.pdfpro.android.library)
    alias(libs.plugins.pdfpro.android.library.compose)
}

android {
    namespace = "com.panwar2001.pdfpro.feature.home"
}

dependencies {
    api(projects.core.ui)
    androidTestImplementation(libs.androidx.espresso.core)
}