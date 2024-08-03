pluginManagement {
    repositories {
        maven("https://nexus.flawcra.cc/repository/maven-mirrors/") { name = "FlawCra" }
    }
    plugins {
        val loom_version: String by settings
        id("fabric-loom").version(loom_version)
        val kotlin_version: String by System.getProperties()
        kotlin("jvm").version(kotlin_version)
    }
}