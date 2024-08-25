
import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import com.panwar2001.pdfpro.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    private val devToolsKspPluginId= "com.google.devtools.ksp"
    private val implementation= "implementation"
    private val ksp = "ksp"
    /**
     * Dependencies
     */
    private val androidxRoomCompiler= "androidx.room.compiler"
    private val androidxRoomKtx= "androidx.room.ktx"
    private val androidxRoomRuntime= "androidx.room.runtime"

    override fun apply(target: Project) {
        with(target) {
            /**
             * room plugin used to simplify auto migration
             */
//            pluginManager.apply("androidx.room")
            pluginManager.apply(devToolsKspPluginId)

            extensions.configure<KspExtension> {
                arg("room.generateKotlin", "true")
            }

            dependencies {
                add(implementation, libs.findLibrary(androidxRoomRuntime).get())
                add(implementation, libs.findLibrary(androidxRoomKtx).get())
                add(ksp, libs.findLibrary(androidxRoomCompiler).get())
            }
        }
    }
}