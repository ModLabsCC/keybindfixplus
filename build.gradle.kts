plugins {
    id("fabric-loom")
    id("com.modrinth.minotaur") version "2.+"
    val kotlin_version: String by System.getProperties()
    kotlin("jvm").version(kotlin_version)
}
base {
    val archives_base_name: String by project
    archivesName.set(archives_base_name)
}
val mod_version: String by project
version = mod_version
val maven_group: String by project
group = maven_group
repositories {

}
dependencies {
    val minecraft_version: String by project
    minecraft("com.mojang:minecraft:$minecraft_version")
    val yarn_mappings: String by project
    mappings("net.fabricmc:yarn:$yarn_mappings:v2")
    val loader_version: String by project
    modImplementation("net.fabricmc:fabric-loader:$loader_version")
    val fabric_version: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_version")
    val fabric_kotlin_version: String by project
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabric_kotlin_version")
}
tasks {
    val javaVersion = JavaVersion.VERSION_21
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions { jvmTarget = javaVersion.toString() }
    }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") { expand(mutableMapOf(
            "version" to project.version,
            "fabric_kotlin_version" to project.extra["fabric_kotlin_version"].toString(),
            "minecraft_version" to project.extra["minecraft_version"].toString(),
            "loader_version" to project.extra["loader_version"].toString(),
        )) }
    }
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN")) // Remember to have the MODRINTH_TOKEN environment variable set or else this will fail - just make sure it stays private!
    projectId.set(project.extra["modrinth_id"].toString()) // This can be the project ID or the slug. Either will work!
    versionNumber.set(mod_version) // You don't need to set this manually. Will fail if Modrinth has this version already
    uploadFile.set(tasks.remapJar) // With Loom, this MUST be set to `remapJar` instead of `jar`!
    gameVersions.addAll(listOf(project.extra["minecraft_version"].toString()))
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
    }
}
