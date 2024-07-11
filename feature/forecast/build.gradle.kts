plugins {
    alias (libs.plugins.weather.android.library)
    alias (libs.plugins.weather.android.compose.library)
    alias (libs.plugins.kotlinx.ksp)
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.weather.feature.forecast"
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

    implementation(project(":core:repository"))
    implementation(project(":core:design"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":sync:work"))

    implementation(libs.androidx.navigationCompose)

    implementation(libs.kotlix.serialization)

    implementation(libs.coilCompose)

    implementation(libs.haze)

    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.androidx.lifecycle.composeRuntime)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.viewModelKtx)
    implementation(libs.androidx.lifecycle.liveDataKtx)

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.work)
    implementation(libs.androidx.datastore)
    implementation(libs.timberLogger)

    androidTestImplementation(project(":core:testing"))
    testImplementation(project(":core:testing"))
}