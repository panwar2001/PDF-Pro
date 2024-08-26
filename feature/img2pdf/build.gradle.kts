plugins {
    alias(libs.plugins.pdfpro.android.library)
    alias(libs.plugins.pdfpro.android.library.compose)
    alias(libs.plugins.pdfpro.hilt)
}

android {
    namespace = "com.panwar2001.pdfpro.feature.img2pdf"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.data)

    implementation(libs.gms.play.services.mlkit.document.scanner)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}