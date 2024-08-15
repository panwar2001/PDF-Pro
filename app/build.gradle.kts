plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.panwar2001.pdfpro"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.panwar2001.pdfpro"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    sourceSets{
        named("test"){
            java.srcDir("src/test/java")
        }
    }
    // Always show the result of every unit test, even if it passes.

    testOptions.unitTests {
        isIncludeAndroidResources = true

        all { test ->

            with(test) {
                testLogging {
                    showCauses=false
                    showExceptions=false
                    showStackTraces=false
                    showStandardStreams=false
                    events = setOf(
                        org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
                    )

                }
                addTestListener(object : TestListener {
                    override fun beforeSuite(suite: TestDescriptor) {
                        // No action needed before suite
                    }

                    override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                        if (suite.parent == null) {
                            // Print results after suite
                            println("Test suite '${suite.name}' finished")
                            val output = "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} passed, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped)"
                            val startItem = "|  "
                            val endItem = "  |"
                            val repeatLength = startItem.length + output.length + endItem.length
                            val line = "-".repeat(repeatLength)
                            println("\n$line\n$startItem$output$endItem\n$line")
                        }

                    }

                    override fun beforeTest(testDescriptor: TestDescriptor) {
                        // No action needed before each test
                    }

                    override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                        // Optionally, you could use this method to track individual test results
                    }
                })

            }

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
    implementation(project(":core:data"))
    implementation(project(":feature:home"))
    implementation(project(":feature:onboard"))
    implementation(project(":feature:settings"))
    /**
     * Android Dependencies
     */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.appcompat)
    /**
     * Hilt Dependencies
     */
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
}


