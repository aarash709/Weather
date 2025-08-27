package com.experiment.buildlogic.conventionplugins

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project


internal fun Project.configureAndroid(
    extension: CommonExtension<*, *, *, *, *, *>,
) {
    extension.apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 26
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}
