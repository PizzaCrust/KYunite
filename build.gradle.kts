plugins {
    kotlin("jvm") version "1.3.72"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup.retrofit2:converter-gson:2.8.1")
    implementation("com.google.guava:guava:29.0-jre")
}