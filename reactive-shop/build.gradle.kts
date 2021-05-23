import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}

group = "ru.nsychev.sd.drawer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    implementation("io.insert-koin:koin-core:3.0.1")

    implementation("io.reactivex:rxjava:1.2.7")
    implementation("org.mongodb:mongodb-driver-rx:1.3.1")
    implementation("io.reactivex:rxnetty-common:0.5.2")
    implementation("io.reactivex:rxnetty-http:0.5.3")
    implementation("io.netty:netty-all:4.1.65.Final")
    implementation("org.slf4j:slf4j-simple:1.7.30")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
