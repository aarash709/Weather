plugins {
    id ("weather.android.application")
    id ("weather.compose.application")
    id ("org.jetbrains.kotlin.android")
    id ("dagger.hilt.android.plugin")
    id ("org.jetbrains.kotlin.plugin.serialization")
    id (libs.plugins.kotlinx.ksp.get().pluginId)
    id ("kotlin-kapt")
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = "com.experiment.weather"

    compileSdk = 34

    defaultConfig {
        applicationId = "com.experiment.weather"
        minSdk = 26
        targetSdk  = 34
        versionCode = 1
        versionName = "0.1-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        //useIR = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}
hilt {
    enableAggregatingTask = true
}

dependencies {

    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(project(":core:design"))
    implementation(project(":core:repository"))
    implementation(project(":core:common"))
    implementation(project(":feature:forecast"))
    implementation(project(":feature:search"))
    implementation(project(":feature:managelocations"))
    implementation(project(":sync:work"))
    implementation(project(":feature:settings"))

    //Kotlin-Core
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.splashScreen)


    implementation(libs.material)
    implementation(libs.kotlix.serialization)

    //implementation "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutine_google_version"
    implementation(libs.kotlix.coroutinesCore)
    implementation(libs.kotlix.coroutinesAndroid)

    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.material)

    implementation(libs.androidx.window)
    implementation(libs.accompanist.systemUIController)

    implementation(libs.androidx.work)

    implementation(libs.androidx.navigationCompose)

    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.lifecycleComposeRuntime)
    implementation(libs.androidx.lifecycleViewModelCompose)

    implementation(libs.androidx.lifecycleViewModelKtx)
    implementation(libs.androidx.lifecycleLiveDataKtx)
    implementation(libs.androidx.activityCompose)

    implementation(libs.androidx.datastore)


    implementation(libs.hilt.navigationCompose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.kaptCompiler)

    implementation(libs.androidx.hiltWork)
    kapt(libs.androidx.hiltKaptCompiler)

    implementation(libs.timberLogger)

    //Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
    androidTestImplementation(libs.compose.ui.test)
}