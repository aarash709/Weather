plugins {
    id("weather.android.library")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
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
    implementation(libs.androidx.lifecycleRuntimeKtx) //fixed duplicate class error while running android test.
    implementation(libs.androidx.datastore)
    implementation(libs.kotlix.serialization)
    implementation(libs.hilt.android)
    kapt(libs.hilt.kaptCompiler)

    testImplementation(libs.kotlix.coroutinesTest)
    testImplementation(libs.junit)
    androidTestImplementation(libs.kotlix.coroutinesTest)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
}