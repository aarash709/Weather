dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
pluginManagement{
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
rootProject.name = "Weather"
include(":app")
include(":core:database")
include(":feature:forecast")
include(":core:model")
include(":core:network")
include(":core:repository")
include(":core:design")
include(":feature:search")
include(":sync:work")
include(":feature:settings")
include(":feature:managelocations")
include(":core:datastore")
include(":feature:settings")
include(":core:common")
