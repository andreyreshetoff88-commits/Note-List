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
include(":groups:groups_data")
include(":groups:groups_presentation")
include(":groups:groups_domain")
include(":profile:profile_data")
include(":profile:profile_domain")
include(":profile:profile_presentation")
include(":verify_email:verify_email_data")
include(":verify_email:verify_email_domain")
include(":verify_email:verify_email_presentation")
