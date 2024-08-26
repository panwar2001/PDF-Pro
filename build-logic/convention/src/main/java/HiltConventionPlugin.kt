

import com.android.build.gradle.api.AndroidBasePlugin
import com.panwar2001.pdfpro.convention.libs
import dagger.hilt.android.plugin.HiltExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    private val devToolsKspPluginId= "com.google.devtools.ksp"
    private val implementation= "implementation"
    private val ksp = "ksp"
    private val androidBase= "com.android.base"
    private val daggerHiltPlugin= "com.google.dagger.hilt.android"
    private val hiltAndroidCompiler= "hilt.android.compiler"
    private val hiltAndroid = "hilt.android"
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(devToolsKspPluginId)
            dependencies {
                add(ksp, libs.findLibrary(hiltAndroidCompiler).get())
            }

            /** Add support for Android modules, based on [AndroidBasePlugin] */
            pluginManager.withPlugin(androidBase) {
                pluginManager.apply(daggerHiltPlugin)
                dependencies {
                    add(implementation, libs.findLibrary(hiltAndroid).get())
                }
                /**
                 * https://dagger.dev/hilt/gradle-setup#aggregating-task
                 */
                extensions.configure<HiltExtension> {
                 enableAggregatingTask=true
                }
            }
        }
    }
}
