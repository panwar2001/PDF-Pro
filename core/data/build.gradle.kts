plugins {
    alias(libs.plugins.pdfpro.android.library)
    alias(libs.plugins.pdfpro.hilt)
    alias(libs.plugins.pdfpro.android.room)
}

android {
    namespace = "com.panwar2001.pdfpro.core.data"
}

dependencies {
    api(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":core:screens"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.documentfile)
    implementation(libs.mockk)
}