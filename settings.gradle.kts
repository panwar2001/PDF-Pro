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

rootProject.name = "PDF Pro"
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
include(":feature:pdf2txt")
include(":feature:languagepicker")
include(":feature:pdf2img")
include(":feature:lockpdf")
include(":feature:pdfviewer")
include(":feature:filepicker")
include(":feature:settings")
include(":feature:home")
