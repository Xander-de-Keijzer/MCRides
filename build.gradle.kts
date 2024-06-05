plugins {
    kotlin("jvm") version "1.9.23"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "nl.xanderdekeijzer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.jar {
    destinationDirectory.set(file("C:\\Users\\xande\\Documents\\Server\\plugins"))
}
