plugins {
    `java-library`

    id("io.papermc.paperweight.userdev") version "1.5.1-LOCAL-SNAPSHOT"
}

group = "com.github.atomishere"
version = "1.0.0-SNAPSHOT"
description = "RPG Plugin in a similar vein of Wynncraft and Hypixel Skyblock"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")
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
}