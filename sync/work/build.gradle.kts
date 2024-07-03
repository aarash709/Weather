plugins {
    alias (libs.plugins.weather.android.library)
    alias (libs.plugins.kotlinx.ksp)
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.weather.sync.work"
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

    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:repository"))
    implementation(project(":core:model"))

//    implementation(libs.androidx.lifecycleLiveDataKtx)
    implementation(libs.kotlix.serialization)
    implementation(libs.androidx.work)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.hiltWork)
    ksp(libs.androidx.hiltKaptCompiler)

    implementation(libs.timberLogger)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
}