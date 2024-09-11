plugins {
    alias(libs.plugins.weather.android.library)
    alias(libs.plugins.secrets)
    alias(libs.plugins.kotlinx.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinSerialization)
}
android {
    namespace = "com.weather.core.network"

    buildFeatures {
        buildConfig = true
    }
    buildTypes {

    }

    kotlinOptions {
        jvmTarget = "17"
    }
    secrets {
        defaultPropertiesFileName = "secrets.default.properties"
    }
}
dependencies {

    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.androidx.lifecycle.runtimeKtx) //fixed duplicate class error while running android test.

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation(libs.retrofitMoshi)
    implementation(libs.moshi)
    implementation(libs.moshiKotlin)

    implementation(libs.kotlix.serialization)

    implementation(libs.timberLogger)

    implementation(libs.bundles.ktor)


    testImplementation(project(":core:testing"))
}