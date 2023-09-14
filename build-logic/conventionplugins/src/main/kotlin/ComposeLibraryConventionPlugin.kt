import com.android.build.api.dsl.LibraryExtension
import com.experiment.buildlogic.conventionplugins.composeBuildConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            extensions.configure<LibraryExtension> {
                compileSdk = 34
                defaultConfig.targetSdk = 34
                composeBuildConfiguration(this)
            }
        }
    }
}