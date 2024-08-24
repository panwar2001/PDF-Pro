package com.panwar2001.pdfpro.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureAndroid(commonExtension: CommonExtension<*, *, *, *, * ,*>) {
    commonExtension.apply {
        compileSdk = 35

        defaultConfig {
            minSdk = 24
            testInstrumentationRunner = "com.google.samples.modularization.testing.HiltTestRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        buildFeatures.buildConfig = false
    }
}
