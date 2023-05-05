plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}
android {
    namespace = "com.weather.core.network"
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
    secrets{
        defaultPropertiesFileName = "secrets.default.properties"
    }
}
dependencies {

    implementation(project(":core:model"))

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.kaptCompiler)
    //implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation(libs.retrofitMoshi)
    implementation(libs.moshi)
    implementation(libs.moshiKotlin)
    implementation(libs.kotlix.serialization)
    implementation(libs.timberLogger)

//    implementation("io.ktor:ktor-client-core:$ktor_version")
//    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
//    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
//    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
//    implementation("io.ktor:ktor-client-android:$ktor_version")
//    implementation("io.ktor:ktor-client-logging:$ktor_version")
//    implementation("io.ktor:ktor-client-cio:$ktor_version")
//    implementation("io.ktor:ktor-client-okhttp:$ktor_version")

    //test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}