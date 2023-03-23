dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs"){
            version("compose.bom", "2023.03.00")
            version("compose-version", "1.4.0")
            library("compose-bom","androidx.compose","compose.bom").versionRef("compose.bom")
            library("compose-animation","androidx.compose.animation","animation").versionRef("compose-version")
            library("compose-foundation","androidx.compose.foundation","foundation").versionRef("compose-version")
            library("compose-foundation","androidx.compose.ui","ui").versionRef("compose-version")
            library("compose-foundation","androidx.compose.ui","ui").versionRef("compose-version")
            library("compose-foundation","androidx.compose.ui","ui-tooling").versionRef("compose-version")
            library("compose-foundation","androidx.compose.runtime","runtime").versionRef("compose-version")
            library("compose-foundation","androidx.compose.runtime","runtime-livedata").versionRef("compose-version")
            library("compose-foundation","androidx.compose.material","material").versionRef("compose-version")
            library("compose-foundation","androidx.compose.material","material-icons-extended").versionRef("compose-version")
        }
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
include(":feature:managelocations")
