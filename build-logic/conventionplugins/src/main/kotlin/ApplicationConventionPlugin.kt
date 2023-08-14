import com.android.build.api.dsl.ApplicationExtension
import com.experiment.buildlogic.conventionplugins.configureAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.getByType<ApplicationExtension>().apply {
                configureAndroid(this)
                defaultConfig {
                    targetSdk = 33
                }
            }
        }
    }
}


