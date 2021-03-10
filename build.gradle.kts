plugins {
    kotlin("multiplatform") version "1.4.31" apply false
    kotlin("plugin.serialization") version "1.4.31" apply false
}

allprojects {
    version = "0.0.1"

    repositories {
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlinx")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }
}