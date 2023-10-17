plugins {
    alias (libs.plugins.weather.android.library)
    alias (libs.plugins.weather.android.compose.library)
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
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
    implementation(project(":feature:forecast")) //needs removal
    implementation(project(":feature:search")) //needs removal
    implementation(project(":core:model"))

    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.animation)

    implementation(libs.androidx.navigationCompose)

    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.lifecycleComposeRuntime)
    implementation(libs.androidx.lifecycleViewModelCompose)
    implementation(libs.androidx.lifecycleViewModelKtx)

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.kaptCompiler)

    implementation(libs.kotlix.serialization)
    implementation(libs.androidx.datastore)
    implementation(libs.timberLogger)

    androidTestImplementation(project(":core:testing"))
    testImplementation(project(":core:testing"))
}