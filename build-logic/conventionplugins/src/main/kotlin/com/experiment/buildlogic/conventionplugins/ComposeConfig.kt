package com.experiment.buildlogic.conventionplugins

import com.android.build.api.dsl.CommonExtension
import getLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.composeBuildConfiguration(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures{
            compose = true
        }
        composeOptions{
            kotlinCompilerExtensionVersion = getLibs.findVersion("androidxComposeCompiler").get().toString()
        }
//
        dependencies {
            val composeBom = getLibs.findLibrary("compose-bom").get()
            add("implementation",platform(composeBom))
            add("androidTestImplementation",platform(composeBom))
        }
    }
}