plugins {
    id("weather.android.library")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id (libs.plugins.kotlinx.ksp.get().pluginId)
    id("kotlin-kapt")
}

android {
    namespace = "com.weather.core.database"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {

    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.kaptCompiler)

    implementation(libs.androidx.lifecycleRuntimeKtx) //fixed duplicate class error while running android test.

    implementation(libs.androidx.roomCommon)
    implementation(libs.androidx.roomKtx)
    ksp(libs.androidx.roomCompiler)
    implementation(libs.androidx.roomRuntime)

    //test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
}