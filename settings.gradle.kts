/*
 * This file was generated by the Gradle 'init' task.
 */

rootProject.name = "autoapi"

pluginManagement {
    resolutionStrategy {
        repositories {
            gradlePluginPortal()
            maven("https://dl.bintray.com/kotlin/kotlin-eap")
            maven("https://dl.bintray.com/kotlin/kotlin-dev")
        }
    }
}

include("shared", "server", "client")