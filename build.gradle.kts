import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
}

group = "one.devsky"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://jitpack.io")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0")
    compileOnly("org.purpurmc.purpur", "purpur-api", "1.18.2-R0.1-SNAPSHOT")
    implementation("net.oneandone.reflections8:reflections8:0.11.7")
    implementation("com.github.CoasterFreakDE:minecraft-spigot-rgb-chat-support:1.0.5")
}



tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}