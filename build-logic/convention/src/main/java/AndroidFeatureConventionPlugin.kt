import com.android.build.gradle.LibraryExtension
import com.panwar2001.pdfpro.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    /**
     * Plugins
     */
    private val implementationConfiguration= "implementation"
    private val androidLibraryPluginId= "pdfpro.android.library"
    private val hiltPluginId="pdfpro.hilt"
    private val detektConfig= "pdfpro.config.detekt"
    /**
     * Dependencies
     */
    private val coreUi = ":core:ui"
    private val androidxHiltNavigationCompose = "androidx.hilt.navigation.compose"
    private val androidxLifecycleRuntimeCompose = "androidx.lifecycle.runtime.ktx"
    private val androidxLifecycleViewModelCompose= "androidx.lifecycle.viewmodel.ktx"

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(androidLibraryPluginId)
                apply(hiltPluginId)
                apply(detektConfig)
            }
            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
            }

            dependencies {
                add(implementationConfiguration, project(coreUi))

                add(implementationConfiguration, libs.findLibrary(androidxHiltNavigationCompose).get())
                add(implementationConfiguration, libs.findLibrary(androidxLifecycleRuntimeCompose).get())
                add(implementationConfiguration, libs.findLibrary(androidxLifecycleViewModelCompose).get())
            }
        }
    }
}
