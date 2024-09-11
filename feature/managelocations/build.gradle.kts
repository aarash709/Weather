plugins {
    alias (libs.plugins.weather.android.library)
    alias (libs.plugins.weather.android.compose.library)
    alias (libs.plugins.kotlinx.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.weather.feature.managelocations"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
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

    implementation(libs.androidx.navigationCompose)

    implementation(libs.coilCompose)

    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.androidx.lifecycle.composeRuntime)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.viewModelKtx)

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlix.serialization)
    implementation(libs.androidx.datastore)
    implementation(libs.timberLogger)

    androidTestImplementation(project(":core:testing"))
    testImplementation(project(":core:testing"))
}