plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val kotlinxSerializationVersion = project.property("kotlinx.serialization.version") as String
val kotlinxCoroutinesVersion = project.property("kotlinx.coroutines.version") as String
val ktorVersion = project.property("ktor.version") as String
val kotlinWrappersSuffix = project.property("kotlin.wrappers.suffix") as String

kotlin {
    jvm()
    js {
        useCommonJs()
        browser {
        }
        binaries.executable()

    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
                implementation("org.jetbrains:kotlin-css:1.0.0-$kotlinWrappersSuffix")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-client-apache:$ktorVersion")
                implementation("org.jetbrains:kotlin-css:1.0.0-$kotlinWrappersSuffix")

            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$kotlinxCoroutinesVersion")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}