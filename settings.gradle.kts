pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Note List"
include(":app")
include(":core")
include(":splash:data")
include(":splash:domain")
include(":splash:presintation")
include(":auth:data")
include(":auth:presintation")
include(":auth:domain")
include(":groups:data")
include(":groups:presentation")
include(":groups:domain")
