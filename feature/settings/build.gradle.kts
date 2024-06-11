plugins {
    alias (libs.plugins.weather.android.library)
    alias (libs.plugins.weather.android.compose.library)
    alias (libs.plugins.kotlinx.ksp)
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.weather.feature.settings"

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
    implementation(project(":sync:work"))

    implementation(libs.androidx.navigationCompose)

    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.lifecycleComposeRuntime)
    implementation(libs.androidx.lifecycleViewModelCompose)
    implementation(libs.androidx.lifecycleViewModelKtx)

    implementation(libs.androidx.work)

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.kaptCompiler)

    implementation(libs.androidx.datastore)
    implementation(libs.timberLogger)


    androidTestImplementation(project(":core:testing"))
    testImplementation(project(":core:testing"))
}