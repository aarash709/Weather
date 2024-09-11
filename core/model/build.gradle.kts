plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinx.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
    implementation(libs.androidx.annotation)
    api(platform(libs.arrow.stack))
    api(libs.arrow.optics)
    ksp(libs.arrow.optics.ksp)

    implementation(libs.kotlix.serialization)
}