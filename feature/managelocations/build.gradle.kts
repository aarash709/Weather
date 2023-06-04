import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("compose.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.weather.feature.managelocations"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(project(":core:repository"))
    implementation(project(":core:design"))
    implementation(project(":feature:forecast")) //needs removal
    implementation(project(":feature:search")) //needs removal
    implementation(project(":core:model"))

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

    testImplementation("junit:junit:4.13.2")
    testImplementation(libs.kotlix.coroutinesTest)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}