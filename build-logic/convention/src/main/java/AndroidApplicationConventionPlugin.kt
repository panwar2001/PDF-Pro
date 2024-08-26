import com.android.build.api.dsl.ApplicationExtension
import com.panwar2001.pdfpro.convention.configureAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    private val androidApplicationPluginId = "com.android.application"
    private val kotlinAndroidPluginId = "org.jetbrains.kotlin.android"
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(androidApplicationPluginId)
                apply(kotlinAndroidPluginId)
            }

            extensions.configure<ApplicationExtension> {
                configureAndroid(this)
                defaultConfig.targetSdk = 35
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
            }
        }
    }
}
