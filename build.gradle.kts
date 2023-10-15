// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins{
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.andoirdGradle) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlinGradle) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.kotlinx.ksp) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlinter) apply false
}
buildscript {

    repositories {
        google()
        mavenCentral()
    }
}
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

}
tasks.withType(io.gitlab.arturbosch.detekt.Detekt::class.java).configureEach {
    reports{
        html.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}
task("clean", Delete::class){
    delete(rootProject.buildDir)
}