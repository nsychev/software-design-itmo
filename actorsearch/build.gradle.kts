import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    scala
    application
}

group = "ru.nsychev.sd.actorsearch"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.scala-lang:scala-library:2.13.5")
    implementation("com.typesafe.akka:akka-actor_2.13:2.6.14")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    implementation("io.ktor:ktor-client-core:1.5.4")
    implementation("io.ktor:ktor-client-cio:1.5.4")
    implementation("io.ktor:ktor-client-serialization:1.5.4")
    implementation("com.github.javafaker:javafaker:1.0.2")

    implementation("org.slf4j:slf4j-nop:1.7.30")

    implementation("io.ktor:ktor-server-core:1.5.4")
    implementation("io.ktor:ktor-server-netty:1.5.4")
    implementation("io.ktor:ktor-serialization:1.5.4")

}

application {
    mainClass.set("ru.nsychev.sd.actorsearch.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

/*tasks.withType<ScalaCompile>() {

}*/

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
