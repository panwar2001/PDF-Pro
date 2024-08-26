plugins {
    alias(libs.plugins.pdfpro.android.library)
    alias(libs.plugins.pdfpro.hilt)
}

android {
    namespace = "com.panwar2001.pdfpro.core.datastore"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    api(projects.core.model)
    testImplementation(libs.junit)
}