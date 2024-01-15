plugins {
    alias (libs.plugins.weather.android.library)
    alias (libs.plugins.weather.android.compose.library)
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.weather.core.design"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {

    }
    kotlinOptions {
        jvmTarget = "17"
    }
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
    debugApi(libs.compose.ui.tooling)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}