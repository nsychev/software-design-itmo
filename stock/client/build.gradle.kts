import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "ru.nsychev.sd.stock"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.insert-koin:koin-core:3.0.2")

    implementation("io.ktor:ktor-client-cio:1.5.4")
    implementation("io.ktor:ktor-client-logging:1.5.4")

    implementation("io.ktor:ktor-server-core:1.5.4")
    implementation("io.ktor:ktor-server-netty:1.5.4")

    implementation("org.slf4j:slf4j-simple:1.7.30")

    testImplementation(kotlin("test"))
    testImplementation("org.testcontainers:junit-jupiter:1.15.3")
    testImplementation("org.testcontainers:testcontainers:1.15.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
