pluginManagement {
    includeBuild("build-logic")
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

rootProject.name = "pdfpro"
/**
 * keep root project name in non capital letters without space to avoid error
 * enable type-safe project accessors
 */
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:domain")
include(":core:database")
include(":core:model")
include(":core:data")
include(":core:ui")
include(":core:screens")
include(":core:datastore")
include(":feature:onboard")
include(":feature:img2pdf")
include(":feature:languagepicker")
include(":feature:pdf2img")
include(":feature:lockpdf")
include(":feature:pdfviewer")
include(":feature:filepicker")
include(":feature:settings")
include(":feature:home")
include(":feature:pdf2txt:pickpdf")
