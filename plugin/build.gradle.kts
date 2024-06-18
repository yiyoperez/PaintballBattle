import org.apache.tools.ant.filters.ReplaceTokens

val projectVersion = property("version") as String

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    compileOnly("me.clip:placeholderapi:2.11.2")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    compileOnly("com.github.Realizedd:TokenManager:3.2.4") {
        exclude("org.bstats", "bstats-bukkit")
        exclude("net.milkbowl.vault", "VaultAPI")
        exclude("be.maximvdw", "MVdWPlaceholderAPI")
    }
    compileOnly("com.gmail.filoghost.holographicdisplays:holographicdisplays-api:2.4.9")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("PaintballBattle-${projectVersion}.jar")

        //destinationDirectory.set(file("C:\\Users\\yiyo_\\Desktop\\TestServer\\plugins"))
        destinationDirectory.set(file("$rootDir/bin/"))
    }
    processResources {
        filesMatching("**/*.yml") {
            filter<ReplaceTokens>(
                "tokens" to mapOf("version" to projectVersion)
            )
        }
    }
}