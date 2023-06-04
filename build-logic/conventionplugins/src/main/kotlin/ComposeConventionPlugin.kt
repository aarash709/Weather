import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            pluginManager.apply("com.android.application")
            val applicationExtension = extensions.getByType<ApplicationExtension>()
            composeBuildConfiguration(applicationExtension)
        }
    }
}

internal fun Project.composeBuildConfiguration(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    commonExtension.apply {
        buildFeatures{
            compose = true
        }
        composeOptions{
            kotlinCompilerExtensionVersion = libs.findVersion("androidxComposeCompiler").get().toString()
        }
//
        dependencies {
            val composeBom = libs.findLibrary("compose-bom").get()
            add("implementation",platform(composeBom))
            add("androidTestImplementation",platform(composeBom))
        }
    }
}