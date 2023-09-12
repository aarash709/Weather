@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("weather.android.library")
}

android {
    namespace = "com.weather.core.testing"

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {

    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:database"))

    api(libs.androidx.test.core)
    api(libs.kotlix.coroutinesTest)
    api(libs.androidx.test.junit)
    api(libs.junit)
    api(libs.androidx.test.espressoCore)

}