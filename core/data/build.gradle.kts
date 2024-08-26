plugins {
    alias(libs.plugins.pdfpro.android.library)
    alias(libs.plugins.pdfpro.hilt)
    alias(libs.plugins.pdfpro.android.room)
}

android {
    namespace = "com.panwar2001.pdfpro.core.data"
}

dependencies {
    api(projects.core.datastore)
    implementation(projects.core.database)
    implementation(projects.core.screens)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.documentfile)
    implementation(libs.mockk)
}