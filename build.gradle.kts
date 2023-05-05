// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val version =""
    extra.apply {
        set("agp_version" , "7.0.3")
        set("kgp_version" , "1.5.21")
        set("compose_version" , "1.4.0") //using bom
        set("compose_bom_version" , "2023.03.00")
        set("accompanist_version" , "0.28.0")
        set("compose_nav_version" , "2.5.3")
        set("compose_compiler_version" , "1.4.4")
        set("room_version" , "2.5.0")
        set("lifecycle_version" , "2.5.1")
        set("kotlin_coroutine" , "1.6.4")
        set("moshi_version" , "1.14.0")
        set("retrofit_version" , "2.9.0")
        set("ktor_version" ,"2.0.2")
        set("arrow_version" , "1.1.2")
        set("kotlin_plugin_version" , "1.8.10")
        set("work_version" ,"2.8.0")
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra.get("kotlin_plugin_version")}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${project.extra.get("kotlin_plugin_version")}")
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