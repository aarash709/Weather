import com.android.build.api.dsl.ApplicationExtension
import com.experiment.buildlogic.conventionplugins.configureAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")
            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = 36
                configureAndroid(this)
            }
        }
    }
}


