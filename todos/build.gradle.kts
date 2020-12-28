import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "ru.nsychev.sd.todos"
version = "1.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("io.ktor:ktor-server-jetty:1.3.2")
    implementation("io.ktor:ktor-freemarker:1.3.2")

    implementation("org.koin:koin-core:2.0.0")
    implementation("org.koin:koin-ktor:2.0.0")

    implementation("org.jetbrains.exposed:exposed-core:0.24.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.24.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.24.1")
    implementation("org.xerial:sqlite-jdbc:3.34.0")

    implementation("ch.qos.logback:logback-core:1.2.3")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.slf4j:slf4j-api:1.7.30")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
