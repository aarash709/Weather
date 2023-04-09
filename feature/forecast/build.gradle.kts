plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.weather.feature.forecast"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles = "consumer-rules.pro"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra.get("compose_compiler_version") as String
    }
}

dependencies {

    implementation(project(":core:repository"))
    implementation(project(":core:design"))
    implementation(project(":core:model"))
    implementation(project(":sync:work"))

    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.material)
    implementation(libs.compose.material.iconsExtended)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui)
    implementation(libs.compose.animation)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.androidx.navigationCompose)
    implementation(libs.accompanist.navigationAnimation)

    implementation(libs.kotlix.serialization)

    implementation(libs.coilCompose)

    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.lifecycleComposeRuntime)
    implementation(libs.androidx.lifecycleViewModelCompose)
    implementation(libs.androidx.lifecycleViewModelKtx)
    implementation(libs.androidx.lifecycleLiveDataKtx)

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.kaptCompiler)

    implementation(libs.androidx.work)
    implementation(libs.androidx.datastore)
    implementation(libs.timberLogger)

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("org.mockito:mockito-core:5.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}