plugins {
    kotlin("js")
    kotlin("plugin.serialization")
    id("com.github.turansky.kfc.definitions") version "2.25.0" // to strip kotlin.js from definitions - e.g. kotlin.js.Promise<> to Promise<>
}

val kotlinxSerializationVersion = project.property("kotlinx.serialization.version") as String
val kotlinxCoroutinesVersion = project.property("kotlinx.coroutines.version") as String
val kotlinWrappersSuffix = project.property("kotlin.wrappers.suffix") as String
val ktorVersion = project.property("ktor.version") as String

kotlin {
    js {
        useCommonJs()
        browser {
        }
        binaries.executable()
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-js")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-js:$kotlinxSerializationVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$kotlinxCoroutinesVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-js:$ktorVersion")

    implementation(project(":shared"))

}

tasks.named("run") {
    dependsOn(":server:prepareDevServer")
}