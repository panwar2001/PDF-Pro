import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.dagger.hilt.android)  apply false
    alias(libs.plugins.devtools.ksp)  apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.detekt) apply false
}
allprojects {
    tasks.register<Detekt>("detektFix") {
        doLast {
            buildUponDefaultConfig=true
            description = ""
            ignoreFailures = false
            autoCorrect = true
            parallel = true
        }
    }
}