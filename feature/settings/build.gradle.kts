plugins {
    id("weather.compose.library")
    id("weather.android.library")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.weather.feature.settings"

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
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
    implementation(project(":core:repository"))
    implementation(project(":core:design"))
    implementation(project(":core:model"))
    implementation(project(":sync:work"))

    //Compose
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.animation)

    implementation(libs.androidx.navigationCompose)

    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.lifecycleComposeRuntime)
    implementation(libs.androidx.lifecycleViewModelCompose)
    implementation(libs.androidx.lifecycleViewModelKtx)

    implementation(libs.androidx.work)

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.kaptCompiler)

    implementation(libs.androidx.datastore)
    implementation(libs.timberLogger)


    androidTestImplementation(project(":core:testing"))
    testImplementation(project(":core:testing"))
}