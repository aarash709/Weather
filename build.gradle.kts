// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins{
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.andoirdGradle) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlinGradle) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.kotlinx.ksp) apply false
}
buildscript {

    repositories {
        google()
        mavenCentral()
    }
}

task("clean", Delete::class){
    delete(rootProject.buildDir)
}