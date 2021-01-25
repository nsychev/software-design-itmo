import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    application
}

group = "ru.nsychev.sd.feed"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.uchuhimo:konf:1.0.0")
    implementation("io.ktor:ktor-client-cio:1.5.0")
    implementation("io.ktor:ktor-client-jackson:1.5.0")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("org.mockito:mockito-core:3.7.7")
    testImplementation("org.mock-server:mockserver-netty:5.11.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClassName = "ru.nsychev.sd.feed.MainKt"
}
