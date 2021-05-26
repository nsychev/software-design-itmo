import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm")
    id("com.palantir.docker") version "0.26.0"
}

group = "ru.nsychev.sd.stock"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:1.5.4")
    implementation("io.ktor:ktor-server-netty:1.5.4")

    implementation("org.slf4j:slf4j-simple:1.7.30")
}

docker {
    name = "nsychev/sd-stock"
    files("build/libs/server-all-1.0.jar")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName.set("${project.name}-all")
    manifest {
        attributes["Main-Class"] = "ru.nsychev.sd.stock.server.ServerKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
    "docker" {
        dependsOn(fatJar)
    }
}
