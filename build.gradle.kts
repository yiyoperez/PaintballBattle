plugins { java }

subprojects {
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
        mavenLocal()

        // JitPack's repository (Used for TokenManager)
        maven("https://jitpack.io")
        // Minecraft's repo
        maven("https://libraries.minecraft.net/")
        // Panda's repo (LiteCommands)
        maven("https://repo.panda-lang.org/releases")
        // CodeMC repo
        maven("https://repo.codemc.io/repository/maven-snapshots/")
        // SpigotMC's repository (Used for Spigot-API)
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        // ExtendedClip's repository (Used for PlaceholderAPI)
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}