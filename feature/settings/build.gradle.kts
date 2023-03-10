plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.weather.feature.settings"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    //navigation
    implementation("androidx.navigation:navigation-compose:${rootProject.extra.get("compose_nav_version")}")
    implementation("com.google.accompanist:accompanist-navigation-animation:${rootProject.extra.get("accompanist_version")}")
    //LifeCycle Components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra.get("lifecycle_version")}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-beta01") //experimental
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra.get("lifecycle_version")}")
    //serializer
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    //work
    implementation("androidx.work:work-runtime-ktx:${rootProject.extra.get("work_version")}")

    //Hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-compiler:2.44.2")
    //datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    //Timber Logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}