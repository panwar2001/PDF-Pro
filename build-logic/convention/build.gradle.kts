import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.panwar2001.pdfpro.convention"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
}

gradlePlugin {
    val androidApplicationCompose="AndroidApplicationComposeConventionPlugin"
    val androidApplicationComposeId= "pdfpro.android.application.compose"

    val androidLibraryCompose= "AndroidLibraryComposeConventionPlugin"
    val androidLibraryComposeId= "pdfpro.android.library.compose"

    val androidApplication= "AndroidApplicationConventionPlugin"
    val androidApplicationId= "pdfpro.android.application"

    val androidLibrary = "AndroidLibraryConventionPlugin"
    val androidLibraryId = "pdfpro.android.library"

    val androidHilt = "HiltConventionPlugin"
    val androidHiltId = "pdfpro.hilt"

    val androidFeature = "AndroidFeatureConventionPlugin"
    val androidFeatureId = "pdfpro.android.feature"

    val androidRoom = "AndroidRoomConventionPlugin"
    val androidRoomId= "pdfpro.android.room"

    val androidTest = "AndroidTestConventionPlugin"
    val androidTestId = "pdfpro.android.test"

    plugins {
        /**
         * Register Compose application and library plugin.
         */
        register(androidApplicationCompose) {
            id = androidApplicationComposeId
            implementationClass = androidApplicationCompose
        }
        register(androidLibraryCompose) {
            id = androidLibraryComposeId
            implementationClass = androidLibraryCompose
        }
        /**
         * Register Android application and library plugin.
         */
        register(androidApplication) {
            id = androidApplicationId
            implementationClass = androidApplication
        }
        register(androidLibrary) {
            id = androidLibraryId
            implementationClass = androidLibrary
        }
        /**
         * Register plugin for features which are build with compose
         */
        register(androidFeature) {
            id = androidFeatureId
            implementationClass = androidFeature
        }
        /**
         * Register hilt plugin
         */
        register(androidHilt) {
            id = androidHiltId
            implementationClass = androidHilt
        }
        /**
         * Register room plugin
         */
        register(androidRoom) {
            id = androidRoomId
            implementationClass = androidRoom
        }
        /**
         * Register test plugin
         */
        register(androidTest) {
            id = androidTestId
            implementationClass = androidTest
        }
    }
}



