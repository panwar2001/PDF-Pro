import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class DetektPlugin: Plugin<Project> {
    private val detektPluginId= "io.gitlab.arturbosch.detekt"
    private val detektPlugins= "detektPlugins"
    private val detektFormattingPluginId= "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6"

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(detektPluginId)
            }
            extensions.configure<DetektExtension> {
                buildUponDefaultConfig=true
                autoCorrect=true
                parallel=true

            }

            dependencies {
                add(detektPlugins,detektFormattingPluginId)
            }
        }
    }
}
