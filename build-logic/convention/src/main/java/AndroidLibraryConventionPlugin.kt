import com.android.build.api.dsl.LibraryExtension
import com.panwar2001.pdfpro.convention.configureAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    private val androidApplicationPluginId = "com.android.application"
    private val kotlinAndroidPluginId = "org.jetbrains.kotlin.android"
    private val testInstrumentationRunner= "androidx.test.runner.AndroidJUnitRunner"
    private val androidTestImplementation= "androidTestImplementation"
    private val testImplementation= "testImplementation"
    private val test="test"
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(androidApplicationPluginId)
                apply(kotlinAndroidPluginId)
            }

            extensions.configure<LibraryExtension> {
                configureAndroid(this)
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
                defaultConfig.testInstrumentationRunner = testInstrumentationRunner
                dependencies {
                    add(androidTestImplementation, kotlin(test))
                    add(testImplementation, kotlin(test))
                }
            }
        }
    }
}
