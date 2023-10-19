@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.weather.android.library)
    alias(libs.plugins.weather.android.compose.library)
}

android {
    namespace = "com.weather.core.testing"
    buildTypes {

    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:design"))

    api(libs.compose.ui.test)
    api(libs.compose.ui.testManifest)
    debugApi(libs.androidx.navigation.testing)
    api(libs.androidx.test.core)
    api(libs.androidx.test.core)
    api(libs.kotlix.coroutinesTest)
    api(libs.androidx.test.junit)
    api(libs.junit)
    api(libs.androidx.test.espressoCore)
    api(libs.hilt.androidTesting)

}