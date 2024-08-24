import com.android.build.api.dsl.LibraryExtension
import com.panwar2001.pdfpro.convention.configureAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureAndroid(this)
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                dependencies {
                    add("androidTestImplementation", kotlin("test"))
                    add("testImplementation", kotlin("test"))
                }
            }

        }
    }
}
