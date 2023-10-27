plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    alias(libs.plugins.kotlinx.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {

    api(platform(libs.arrow.stack))
    api(libs.arrow.optics)
    ksp(libs.arrow.optics.ksp)

    implementation(libs.kotlix.serialization)
}