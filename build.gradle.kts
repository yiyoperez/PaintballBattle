plugins { java }

subprojects {
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
        mavenLocal()

        // JitPack's repository (Used for ACID, sMessages, others..)
        maven("https://jitpack.io")
        // Minecraft repo
        maven("https://libraries.minecraft.net/")
        maven("https://repo.triumphteam.dev/snapshots/")
        // Sonatype's repository (Used for bungee-chat)
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        // SpigotMC's repository (Used for Spigot-API)
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        // ExtendedClip's repository (Used for PlaceholderAPI)
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}