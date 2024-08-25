plugins {
    alias(libs.plugins.pdfpro.android.library)
    alias(libs.plugins.pdfpro.android.room)
    alias(libs.plugins.pdfpro.hilt)
}

android {
    namespace = "com.panwar2001.pdfpro.core.database"
}
dependencies {
    api(project(":core:model"))
}