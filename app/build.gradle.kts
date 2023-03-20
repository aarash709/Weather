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
        targetSdk  = 32
        versionCode = 1
        versionName = "0.1-pre-apha-demo"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        //useIR = true
    }
    buildFeatures {
        compose = true
        dataBinding = false
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra.get("compose_compiler_version") as String
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

    //Kotlin-Core
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    //Arrow
//    implementation platform("io.arrow-kt:arrow-stack:$arrow_version")
//    implementation "io.arrow-kt:arrow-core"
//    implementation "io.arrow-kt:arrow-fx-coroutines"
//    kapt "io.arrow-kt:arrow-meta:1.6.0" //bom file version is not set correctly

    //serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    //Coroutines
    //implementation "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutine_google_version"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra.get("kotlin_coroutine")}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra.get("kotlin_coroutine")}")

    //Material
    implementation("com.google.android.material:material:1.8.0")

    //Compose
    implementation(platform("androidx.compose:compose-bom:${rootProject.extra.get("compose_bom_version")}"))
    implementation("androidx.compose.ui:ui:")
    implementation("androidx.compose.material:material:")
    implementation("androidx.compose.material:material-icons-extended:")
    implementation("androidx.compose.ui:ui-tooling-preview:")
    debugImplementation ("androidx.compose.ui:ui-tooling:")
    implementation("androidx.compose.runtime:runtime:")
    implementation("androidx.compose.runtime:runtime-livedata:")
    implementation("androidx.compose.foundation:foundation:")
    implementation("androidx.compose.animation:animation:")

    //window
    implementation("androidx.window:window:1.0.0")

    //work
    implementation("androidx.work:work-runtime-ktx:${rootProject.extra.get("work_version")}")

    //navigation
    implementation("androidx.navigation:navigation-compose:${rootProject.extra.get("compose_nav_version")}")
    implementation("com.google.accompanist:accompanist-navigation-animation:${rootProject.extra.get("accompanist_version")}")

    //LifeCycle Components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra.get("lifecycle_version")}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-beta01") //experimental
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra.get("lifecycle_version")}")

    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra.get("lifecycle_version")}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra.get("lifecycle_version")}")
    implementation("androidx.activity:activity-compose:1.6.1")

    //datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Timber Logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    //Hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-compiler:2.44.2")

    //Hilt work
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    //Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")
    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra.get("compose_version")}")
}