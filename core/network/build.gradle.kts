plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}
android {
    namespace = "com.weather.core.network"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles = "consumer-rules.pro"
    }
    buildFeatures {
        buildConfig = true
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
    secrets{
        defaultPropertiesFileName = "secrets.default.properties"
    }
}
dependencies {

    implementation(project(":core:model"))

    implementation(libs.androidx.lifecycleRuntimeKtx) //fixed duplicate class error while running android test.

    implementation(libs.hilt.android)
    kapt(libs.hilt.kaptCompiler)
    //implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation(libs.retrofitMoshi)
    implementation(libs.moshi)
    implementation(libs.moshiKotlin)
    implementation(libs.kotlix.serialization)
    implementation(libs.timberLogger)

    implementation(libs.bundles.ktor)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
}