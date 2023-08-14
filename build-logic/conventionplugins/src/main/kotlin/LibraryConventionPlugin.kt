import com.android.build.api.dsl.LibraryExtension
import com.experiment.buildlogic.conventionplugins.configureAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<LibraryExtension> {
                configureAndroid(this)
                defaultConfig.targetSdk = 33
            }
        }
    }

}