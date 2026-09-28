
plugins {
    id("org.gradle.java-library")
}

buildscript {
    repositories {
        mavenCentral()
    }
}

repositories {
    mavenLocal()
    mavenCentral()

    maven { url = uri("https://jitpack.io") }
}

allprojects {
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

group = "dev.shared"
version = "0.0.0"
description = "SharedPlugin"

val useLocalDarkBot = providers.gradleProperty("useLocalDarkBot")
    .map(String::toBoolean)
    .getOrElse(true)

dependencies {
    api("eu.darkbot.DarkBotAPI", "darkbot-impl", "0.9.11")
    api("eu.darkbot", "DarkBot", "e787b48c23")
}


tasks.named<Jar>("jar") {
    archiveFileName.set("SharedPlugin.jar")
}

tasks.named("build") {
    doLast {
        copy {
            from(tasks.named<Jar>("jar").flatMap { it.archiveFile })
            into(layout.projectDirectory.dir("../../plugin-builds"))
            rename { "SharedPlugin.jar" }
        }
    }
}

tasks.register<Exec>("signFile") {
    dependsOn("build")
    commandLine("cmd", "/c", "sign.bat")
}
