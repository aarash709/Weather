import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias (libs.plugins.weather.android.library)
    alias (libs.plugins.kotlinx.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.weather.core.database"
    defaultConfig {
        testInstrumentationRunner = "com.weather.core.testing.WeatherTestRunner"
    }
    buildTypes {

    }

    kotlin.compilerOptions.jvmTarget= JvmTarget.JVM_17
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlix.coroutinesAndroid)

    implementation(libs.androidx.lifecycle.runtimeKtx) //fixed duplicate class error while running android test.

    implementation(libs.androidx.roomCommon)
    implementation(libs.androidx.roomKtx)
    ksp(libs.androidx.roomCompiler)
    implementation(libs.androidx.roomRuntime)

    //test
    androidTestImplementation(project(":core:testing")){
        exclude(libs.compose.ui.test.get().group)
    }
}