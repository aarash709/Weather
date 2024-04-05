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
rootProject.name = "weather"
include(":app")
include(":benchmark")

include(":core:database")
include(":core:model")
include(":core:network")
include(":core:repository")
include(":core:design")
include(":feature:search")
include(":core:datastore")
include(":core:common")
include(":core:testing")

include(":sync:work")

include(":feature:forecast")
include(":feature:settings")
include(":feature:managelocations")
include(":feature:settings")
