import org.apache.tools.ant.filters.ReplaceTokens

val projectVersion = property("version") as String

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    compileOnly("com.mojang:authlib:1.5.26")
    compileOnly("me.clip:placeholderapi:2.11.2")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    compileOnly("com.github.Realizedd:TokenManager:3.2.4") {
        exclude("org.bstats", "bstats-bukkit")
        exclude("net.milkbowl.vault", "VaultAPI")
        exclude("be.maximvdw", "MVdWPlaceholderAPI")
    }

    implementation("fr.mrmicky:fastboard:2.1.3")
    implementation("dev.dejvokep:boosted-yaml:1.3.1")
    implementation("dev.triumphteam:triumph-cmd-bukkit:2.0.0-SNAPSHOT")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("PaintballBattle-${projectVersion}.jar")

        destinationDirectory.set(file("C:\\Users\\yiyo_\\Desktop\\TESTS Puffer 1.20.4\\plugins"))
        //destinationDirectory.set(file("$rootDir/bin/"))
    }
    processResources {
        filesMatching("**/*.yml") {
            filter<ReplaceTokens>(
                "tokens" to mapOf("version" to projectVersion)
            )
        }
    }
}