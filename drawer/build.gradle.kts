import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}

group = "ru.nsychev.sd.drawer"
version = "1.0"

repositories {
    mavenCentral()
}

javafx {
    version = "11.0.2"
    modules = listOf("javafx.controls")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClassName = "ru.nsychev.sd.drawer.MainKt"
}
