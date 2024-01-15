@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias (libs.plugins.weather.android.library)
    alias(libs.plugins.kotlinx.ksp)
}

android {
    namespace = "com.experiment.weather.core.common"

    defaultConfig {
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
    implementation(project(":core:model"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.kaptCompiler)

    testImplementation(project(":core:testing"))
}