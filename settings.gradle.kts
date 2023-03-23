dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs"){
            version("composeBom", "2023.03.00")
            version("composeVersion", "1.4.0")
            library("compose-bom","androidx.compose","compose.bom").versionRef("composeBom")
            library("compose-animation","androidx.compose.animation","animation").versionRef("composeVersion")
            library("compose-foundation","androidx.compose.foundation","foundation").versionRef("composeVersion")
            library("compose-ui","androidx.compose.ui","ui").versionRef("composeVersion")
            library("compose-ui-toolingPreview","androidx.compose.ui","ui-tooling-preview").versionRef("composeVersion")
            library("compose-ui-tooling","androidx.compose.ui","ui-tooling").versionRef("composeVersion")
            library("compose-runtime","androidx.compose.runtime","runtime").versionRef("composeVersion")
            library("compose-runtime-livedata","androidx.compose.runtime","runtime-livedata").versionRef("composeVersion")
            library("compose-material","androidx.compose.material","material").versionRef("composeVersion")
            library("compose-material-iconsExtended","androidx.compose.material","material-icons-extended").versionRef("composeVersion")
            bundle("composeUi", listOf("compose-animation","compose-foundation"))
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
