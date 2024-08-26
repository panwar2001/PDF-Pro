plugins {
    alias(libs.plugins.pdfpro.android.library)
    alias(libs.plugins.pdfpro.android.library.compose)
}

android {
    namespace = "com.panwar2001.pdfpro.core.ui"
}

dependencies {
    api(projects.core.model)
    api(projects.core.screens)

    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.play.services.ads)

    api(platform(libs.androidx.compose.bom))
    api(libs.coil.compose)
    api(libs.androidx.material3)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.activity.compose)
    api(libs.androidx.hilt.navigation.compose)
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)

    implementation(libs.android.pdf.viewer)

    androidTestImplementation(libs.androidx.espresso.core)
}