plugins {
    alias (libs.plugins.weather.android.library)
    alias (libs.plugins.weather.android.compose.library)
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.weather.feature.forecast"
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

    implementation(project(":core:repository"))
    implementation(project(":core:design"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":sync:work"))
val picoChart = "1.13.1"
    // For Jetpack Compose.
    implementation("com.patrykandpatrick.vico:compose:$picoChart")

    // For `compose`. Creates a `ChartStyle` based on an M2 Material Theme.
//    implementation("com.patrykandpatrick.vico:compose-m2:1.13.1")

    // For `compose`. Creates a `ChartStyle` based on an M3 Material Theme.
    implementation("com.patrykandpatrick.vico:compose-m3:$picoChart")

    // Houses the core logic for charts and other elements. Included in all other modules.
    implementation("com.patrykandpatrick.vico:core:$picoChart")

    //ycharts
    implementation("co.yml:ycharts:2.1.0")


    implementation(libs.androidx.navigationCompose)

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

    androidTestImplementation(project(":core:testing"))
    testImplementation(project(":core:testing"))
}