import com.android.build.api.dsl.LibraryExtension
import com.experiment.buildlogic.conventionplugins.composeBuildConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class ComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")
            extensions.configure<LibraryExtension> {
                composeBuildConfiguration(this)
            }
        }
    }
}