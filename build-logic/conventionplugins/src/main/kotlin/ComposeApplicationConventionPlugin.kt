import com.android.build.api.dsl.ApplicationExtension
import com.experiment.buildlogic.conventionplugins.composeBuildConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class ComposeApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            pluginManager.apply("com.android.application")
            val applicationExtension = extensions.getByType<ApplicationExtension>()
            composeBuildConfiguration(applicationExtension)
        }
    }
}

