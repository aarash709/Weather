plugins {
    `kotlin-dsl`
//    id("java-library")
//    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies{
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}
gradlePlugin{
    plugins {
        register("applicationCompose"){
            id = "compose.application"
            implementationClass = "ComposeConventionPlugin"
        }
    }
}