plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id ("dagger.hilt.android.plugin")
    id ("org.jetbrains.kotlin.plugin.serialization")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = "com.experiment.weather"

    compileSdk = 33

    defaultConfig {
        applicationId = "com.experiment.weather"
        minSdk = 26
        targetSdk  = 33
        versionCode = 1
        versionName = "0.1-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        //useIR = true
    }
    buildFeatures {
        compose = true
        dataBinding = false
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
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
    implementation(project(":feature:forecast"))
    implementation(project(":feature:search"))
    implementation(project(":feature:managelocations"))
    implementation(project(":sync:work"))
    implementation(project(":feature:settings"))

    //Kotlin-Core
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.splashScreen)

    //Arrow
//    implementation platform("io.arrow-kt:arrow-stack:$arrow_version")
//    implementation "io.arrow-kt:arrow-core"
//    implementation "io.arrow-kt:arrow-fx-coroutines"
//    kapt "io.arrow-kt:arrow-meta:1.6.0" //bom file version is not set correctly
    implementation("com.google.android.material:material:1.8.0")
    implementation(libs.kotlix.serialization)

    //implementation "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutine_google_version"
    implementation(libs.kotlix.coroutinesCore)
    implementation(libs.kotlix.coroutinesAndroid)

    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.material)
    implementation(libs.compose.material.iconsExtended)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.androidx.window)
    implementation(libs.accompanist.systemUIController)

    implementation(libs.androidx.work)

    implementation(libs.androidx.navigationCompose)
    implementation(libs.accompanist.navigationAnimation)

    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.lifecycleComposeRuntime)
    implementation(libs.androidx.lifecycleViewModelCompose)

    implementation(libs.androidx.lifecycleViewModelKtx)
    implementation(libs.androidx.lifecycleLiveDataKtx)
    implementation(libs.androidx.activityCompose)

    implementation(libs.androidx.datastore)

    implementation(libs.timberLogger)

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.kaptCompiler)

    implementation(libs.androidx.hiltWork)
    kapt(libs.androidx.hiltKaptCompiler)

    //Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")
}