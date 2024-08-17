plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    compileSdk = 35
    namespace = "com.panwar2001.pdfpro.pdf2txt.pickpdf"
}

dependencies {
    implementation(project(":core:ui"))
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}