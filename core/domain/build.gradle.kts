plugins {
    alias(libs.plugins.pdfpro.android.library)
    alias(libs.plugins.pdfpro.hilt)
}

android {
    namespace = "com.panwar2001.pdfpro.core.domain"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(projects.core.data)
    implementation(libs.pdfbox.android)
    testImplementation(libs.junit)
}