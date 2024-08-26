package com.panwar2001.pdfpro.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureAndroid(commonExtension: CommonExtension<*, *, *, *, * ,*>) {
    commonExtension.apply {
        compileSdk = 35

        defaultConfig {
            minSdk = 24
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        buildFeatures.buildConfig = false
        kotlinExtension.apply {
            jvmToolchain(8)
        }

    }
}

