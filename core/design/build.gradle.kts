import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias (libs.plugins.weather.android.library)
    alias (libs.plugins.weather.android.compose.library)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.weather.core.design"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {

    }
    kotlin.compilerOptions.jvmTarget= JvmTarget.JVM_17
}

dependencies {

    api(libs.compose.foundation)
    api(libs.compose.animation)
    api(libs.compose.runtime)
    api(libs.compose.material)
    api(libs.compose.material3)
    api(libs.compose.material.iconsExtended)
    api(libs.compose.ui.util)
    api(libs.compose.ui.toolingPreview)

    implementation(libs.kotlix.serialization)

    debugApi(libs.compose.ui.tooling)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}