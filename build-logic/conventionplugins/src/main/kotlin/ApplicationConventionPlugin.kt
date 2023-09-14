import com.android.build.api.dsl.ApplicationExtension
import com.experiment.buildlogic.conventionplugins.configureAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.impldep.com.jcraft.jsch.ConfigRepository.defaultConfig
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<ApplicationExtension> {
                compileSdk = 34
                defaultConfig.targetSdk = 34
                configureAndroid(this)
            }
        }
    }
}


