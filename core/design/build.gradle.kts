plugins {
    id("weather.android.library")
    id ("compose.library")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
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
    api(libs.compose.runtime)
    api(libs.compose.material)
    api(libs.compose.material3)
    api(libs.compose.material.iconsExtended)
    api(libs.compose.ui.toolingPreview)
    debugApi(libs.compose.ui.tooling)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
}