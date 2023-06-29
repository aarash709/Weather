plugins {
    id("compose.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.weather.feature.search"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles = "consumer-rules.pro"
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
}

dependencies {

    implementation(project(":feature:forecast"))
    implementation(project(":core:repository"))
    implementation(project(":core:design"))
    implementation(project(":core:model"))
    implementation(project(":sync:work"))

    //Compose
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.animation)
    implementation(libs.compose.material3)

    implementation(libs.androidx.navigationCompose)

    implementation(libs.accompanist.flowLayout)
    implementation(libs.accompanist.placeholder)

    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.lifecycleComposeRuntime)
    implementation(libs.androidx.lifecycleViewModelCompose)
    implementation(libs.androidx.lifecycleViewModelKtx)

    implementation(libs.kotlix.serialization)
    implementation(libs.androidx.datastore)

    implementation(libs.androidx.work)

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.kaptCompiler)

    implementation(libs.timberLogger)

    testImplementation(libs.junit)
    testImplementation(libs.kotlix.coroutinesTest)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
}