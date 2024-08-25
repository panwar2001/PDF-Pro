
import com.android.build.api.dsl.LibraryExtension
import com.panwar2001.pdfpro.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    private val androidLibraryPluginId = "com.android.library"
    private val kotlinPluginCompose = "org.jetbrains.kotlin.plugin.compose"
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = androidLibraryPluginId)
            apply(plugin = kotlinPluginCompose)

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}