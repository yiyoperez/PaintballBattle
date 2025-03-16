import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("xyz.jpenilla.run-paper") version "2.2.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.github.revxrsal.zapper") version "1.0.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

dependencies {
    compileOnly("com.mojang:authlib:1.5.26")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    compileOnly("com.github.Realizedd:TokenManager:3.2.4") {
        exclude("org.bstats", "bstats-bukkit")
        exclude("net.milkbowl.vault", "VaultAPI")
        exclude("be.maximvdw", "MVdWPlaceholderAPI")
    }

    zap("team.unnamed:inject:2.0.1")
    zap("dev.dejvokep:boosted-yaml:1.3.1")
    zap("dev.triumphteam:triumph-gui:3.1.11")
    zap("dev.rollczi:litecommands-bukkit:3.9.7")
    zap("com.github.cryptomorin:XSeries:11.2.0.1")
    zap("net.megavex:scoreboard-library-api:2.2.2")
    zap("net.kyori:adventure-text-minimessage:4.17.0")

    implementation("net.wesjd:anvilgui:1.10.4-SNAPSHOT")
    runtimeOnly("net.megavex:scoreboard-library-modern:2.2.2")
    runtimeOnly("net.megavex:scoreboard-library-implementation:2.2.2")
}

group = "pb.ajneb97"
description = "PaintballBattle"
version = "2.0.0"

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    shadowJar {
        archiveFileName.set("PaintballBattle-${project.version}.jar")

        destinationDirectory.set(file("$rootDir/bin/"))
    }

    zapper {
        libsFolder = "libraries"
        relocationPrefix = "${group}.libs"

        repositories {
            // Used on AnvilGUI
            maven("https://repo.codemc.io/repository/maven-snapshots/")
            includeProjectRepositories()
        }

        //RELOCATE
    }

    runServer {
        minecraftVersion("1.21.4")
    }

    bukkit {
        name = "PaintballBattle"
        prefix = name
        version = project.version.toString()
        main = project.group.toString() + ".PaintballBattle"
        apiVersion = "1.16"
        authors = listOf("Ajneb97")
        contributors = listOf("Sliide_")
        softDepend = listOf(
            "Vault",
            "PlaceholderAPI",
            "Multiverse-Core",
        )

        // TODO:
        permissions {
            register("change.this") {
                default = BukkitPluginDescription.Permission.Default.OP
                description = "Ayooo also change this."
                children = listOf(
                    "change.me"
                )
            }
        }
    }
}