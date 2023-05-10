// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {

        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlinPluginVersion.get()}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${libs.versions.kotlinPluginVersion.get()}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:${libs.versions.gradleSecret.get()}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

task("clean", Delete::class){
    delete(rootProject.buildDir)
}
//task clean(type: Delete) {
//    delete(rootProject.buildDir)
//}