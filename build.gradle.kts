plugins {
    kotlin("jvm") version "2.4.0"
    application
}

group = "synth"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
}

application {
    mainClass.set("synth.MainKt")
}

tasks.test {
    useJUnitPlatform()
}
