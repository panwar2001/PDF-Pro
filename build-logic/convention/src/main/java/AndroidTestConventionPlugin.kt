import com.android.build.gradle.TestExtension

import com.panwar2001.pdfpro.convention.extendTest
import com.panwar2001.pdfpro.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidTestConventionPlugin : Plugin<Project> {
    private val kotlinAndroidPluginId = "org.jetbrains.kotlin.android"
    private val implementation = "implementation"
    private val mockk= "mockk"
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<TestExtension> {
                extendTest(this)
                defaultConfig.targetSdk = 34
            }
            dependencies {
                add(implementation, libs.findLibrary(mockk).get())
            }
        }
    }
}

