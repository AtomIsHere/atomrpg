plugins {
    `java-library`

    id("io.papermc.paperweight.userdev") version "1.5.1-LOCAL-SNAPSHOT"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"

    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.github.atomishere"
version = "1.0.0-SNAPSHOT"
description = "RPG Plugin in a similar vein of Wynncraft and Hypixel Skyblock"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.aikar.co/content/groups/aikar/") }
}

dependencies {
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")

    implementation("com.google.inject:guice:5.1.0")
    implementation("io.github.classgraph:classgraph:4.8.154")
    implementation("io.github.toolfactory:narcissus:1.0.7")

    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    shadowJar {
        fun reloc(pkg: String) = relocate(pkg, "com.github.atomishere.atomrpg.depends")

        reloc("co.aikar.commands")
        reloc("co.aikar.locales")
    }
}

bukkit {
    main = "com.github.atomishere.atomrpg.AtomRPG"

    apiVersion = "1.19"

    name = "AtomRPG"
    version = getVersion().toString()
    authors = listOf("AtomIsHere")
    description = "RPG plugin for spigot"

    website = "https://github.com/AtomIsHere"
}