plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("kapt") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.dqmme"
version = "1.2"

repositories {
    mavenCentral()
    maven("https://repo.velocitypowered.com/snapshots/")
}

dependencies {
    //Need to shade stdlib, cant download libraries like at spigot
    implementation(kotlin("stdlib"))

    //Velocity API
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")

    //Annotations
    kapt("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")

    //Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

kotlin {
    jvmToolchain(17)
}