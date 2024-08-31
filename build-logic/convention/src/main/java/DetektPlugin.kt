import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class DetektPlugin: Plugin<Project> {
    private val detektPluginId= "io.gitlab.arturbosch.detekt"
    private val detektPlugins= "detektPlugins"
    private val detektFormattingPluginId= "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6"

    override fun apply(target: Project) {
        val detektConfigFilePath = "${target.rootDir}/detekt.yml" // Update this path to your detekt.yml file

        with(target) {
            with(pluginManager) {
                apply(detektPluginId)
            }
            extensions.configure<DetektExtension> {
                buildUponDefaultConfig=true
                autoCorrect=true
                parallel=true
                config.setFrom(files(detektConfigFilePath)) // Set the path to your detekt.yml file
            }

            dependencies {
                add(detektPlugins,detektFormattingPluginId)
            }
        }
    }
}
