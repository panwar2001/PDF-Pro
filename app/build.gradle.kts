plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.panwar2001.pdfpro"
    compileSdk = 34

    androidResources{
        generateLocaleConfig = true
    }
    defaultConfig {
        applicationId = "com.panwar2001.pdfpro"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            /**
             *  Enables code shrinking, obfuscation, and optimization for only
             *  your project's release build type. Make sure to use a build
             *  variant with `isDebuggable=false`.
             */
            isMinifyEnabled = true
            /**
             * Enables resource shrinking, which is performed by the
             * Android Gradle plugin.
             */
            isShrinkResources = true
            proguardFiles(
                /**
                 *  Includes the default ProGuard rules files that are packaged with
                 *  the Android Gradle plugin. To learn more, go to the section about
                 *  R8 configuration files.
                 */
                getDefaultProguardFile("proguard-android-optimize.txt"),
                // Includes a local, custom Proguard rules file
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // enable java 8 in the project
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    //https://dagger.dev/hilt/gradle-setup#aggregating-task
    hilt {    enableAggregatingTask = true }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.ads)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.material)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.pdfbox.android)
    implementation(libs.android.pdf.viewer)
    implementation(libs.reorderable)
    implementation(libs.hilt.android)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.room.compiler)
    /**
     * Compose Dependencies
     */
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    /**
     * Testing dependencies
     */
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}


