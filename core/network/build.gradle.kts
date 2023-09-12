plugins {
    id("weather.android.library")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}
android {
    namespace = "com.weather.core.network"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles = "consumer-rules.pro"
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {

    }
   
    kotlinOptions {
        jvmTarget = "17"
    }
    secrets {
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


    testImplementation(project(":core:testing"))
}