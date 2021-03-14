import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
}

application {
    mainClass.set("com.malcolmcrum.autoapi.server.SampleServerKt")
}

repositories {
    mavenLocal()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://repo.maven.apache.org/maven2/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("io.ktor:ktor-serialization:1.5.0")
    implementation("io.ktor:ktor-locations:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.30-M1")
    implementation("com.squareup:kotlinpoet:1.7.2")
    implementation("io.ktor:ktor-client-cio:1.5.2")

    implementation(project(":shared"))
}

group = "org.ktbyte"
version = "1.0-SNAPSHOT"
description = "autoapi"
java.sourceCompatibility = JavaVersion.VERSION_11
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.4"
}