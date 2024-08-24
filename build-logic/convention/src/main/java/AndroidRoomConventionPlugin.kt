
import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import com.panwar2001.pdfpro.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            /**
             * room plugin used to simplify auto migration
             */
//            pluginManager.apply("androidx.room")
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<KspExtension> {
                arg("room.generateKotlin", "true")
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx.room.runtime").get())
                add("implementation", libs.findLibrary("androidx.room.ktx").get())
                add("ksp", libs.findLibrary("androidx.room.compiler").get())
            }
        }
    }
}