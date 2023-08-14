plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies{
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin{
    plugins {
        register("compose-application"){
            id = "weather.compose.application"
            implementationClass = "ComposeApplicationConventionPlugin"
        }
        register("compose-library"){
            id = "weather.compose.library"
            implementationClass = "ComposeLibraryConventionPlugin"
        }
        register("application-convention"){
            id = "weather.android.application"
            implementationClass = "ApplicationConventionPlugin"
        }
        register("library-convention"){
            id = "weather.android.library"
            implementationClass = "LibraryConventionPlugin"
        }
    }
}