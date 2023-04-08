val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.10"
    id("io.ktor.plugin") version "2.2.4"
    application
}

group = "xyz.nkomarn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("net.kyori:adventure-api:4.13.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.13.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.13.0")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

application {
    mainClass.set("xyz.nkomarn.prismarine.BootstrapKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}