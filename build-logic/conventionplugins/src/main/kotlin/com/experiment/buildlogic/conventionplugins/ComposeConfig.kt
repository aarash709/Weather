package com.experiment.buildlogic.conventionplugins

import com.android.build.api.dsl.CommonExtension
import getLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.composeBuildConfiguration(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
        dependencies {
            val composeBom = getLibs.findLibrary("compose-bom").get()
            add("implementation", platform(composeBom))
            add("androidTestImplementation", platform(composeBom))
        }
    }
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        enableStrongSkippingMode = true
    }
}