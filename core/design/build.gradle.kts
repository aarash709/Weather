plugins {
    id ("compose.library")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.weather.core.design"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
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