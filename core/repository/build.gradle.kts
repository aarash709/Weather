plugins {
    alias (libs.plugins.weather.android.library)
    alias (libs.plugins.kotlinx.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.weather.core.repository"
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
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.timberLogger)
    implementation(libs.androidx.lifecycle.runtimeKtx) //fixed duplicate class error while running android test.
    implementation(libs.androidx.datastore)
    implementation(libs.kotlix.serialization)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}