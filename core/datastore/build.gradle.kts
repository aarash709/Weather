plugins {
    alias (libs.plugins.weather.android.library)
    alias(libs.plugins.kotlinx.ksp)
}

android {
    namespace = "com.weather.core.datastore"
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

    implementation(libs.androidx.datastore)
    implementation(libs.androidx.lifecycle.runtimeKtx) //fixed duplicate class error while running android test.

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlix.coroutinesAndroid)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}