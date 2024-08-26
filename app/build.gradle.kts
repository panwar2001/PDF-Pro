plugins {
    alias(libs.plugins.pdfpro.android.application)
    alias(libs.plugins.pdfpro.android.application.compose)
    alias(libs.plugins.pdfpro.hilt)
}

android {
    namespace = "com.panwar2001.pdfpro"

    defaultConfig {
        applicationId = "com.panwar2001.pdfpro"
        /**
         * version= X.Y.Z;
         * version code = X * 100 + Y * 10 + Z
         * X = Major, Y = minor, Z = Patch level
         */
        versionName = "1.0.1"
        versionCode = 101

        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        release {
            isDebuggable=false
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    //https://dagger.dev/hilt/gradle-setup#aggregating-task
    hilt {    enableAggregatingTask = true }
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.onboard)
    implementation(projects.feature.settings)
    implementation(projects.feature.img2pdf)
    /**
     * Android Dependencies
     */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.core.splashscreen)
}


