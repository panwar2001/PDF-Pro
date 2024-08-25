
import com.android.build.api.dsl.ApplicationExtension
import com.panwar2001.pdfpro.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

    private val androidApplicationPluginId= "com.android.application"
    private val kotlinComposePluginId = "org.jetbrains.kotlin.plugin.compose"

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = androidApplicationPluginId)
            apply(plugin = kotlinComposePluginId)

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}