

import com.android.build.gradle.api.AndroidBasePlugin
import com.panwar2001.pdfpro.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    private val devToolsKspPluginId= "com.google.devtools.ksp"
    private val implementation= "implementation"
    private val ksp = "ksp"
    private val androidBase= "com.android.base"
    private val daggerHiltPlugin= "com.google.dagger.hilt.android"
    private val hiltAndroidCompiler= "hilt.android.compiler"
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
                    add(implementation, libs.findLibrary(hiltAndroidCompiler).get())
                }
            }
        }
    }
}
