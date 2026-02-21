import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.weather.android.library)
    alias(libs.plugins.kotlinx.ksp)
}

android {
    namespace = "com.experiment.weather.core.common"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
    }
    kotlin.compilerOptions.jvmTarget= JvmTarget.JVM_17
}

dependencies {
    implementation(project(":core:model"))
    api(libs.androidx.annotation)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(project(":core:testing"))
}