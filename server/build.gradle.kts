import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val kotlinxSerializationVersion = project.property("kotlinx.serialization.version") as String
val ktorVersion = project.property("ktor.version") as String


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
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.30-M1")

    implementation(project(":shared"))
    implementation(project(":generator"))
}

group = "org.ktbyte"
version = "1.0-SNAPSHOT"
description = "autoapi"
java.sourceCompatibility = JavaVersion.VERSION_11
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.4"
}