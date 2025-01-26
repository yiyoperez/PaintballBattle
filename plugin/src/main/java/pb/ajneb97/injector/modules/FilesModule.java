package pb.ajneb97.injector.modules;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.message.MessageHandler;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.Named;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("DataFlowIssue")
@InjectAll
public final class FilesModule extends AbstractModule {

    @Override
    protected void configure() {
        Logger.info("Attempting to create plugin files.");
    }

    @Provides
    @Singleton
    @Named("arena-path")
    public Path provideArenasPath(PaintballBattle plugin) throws IOException {
        Path arenasPath = plugin.getDataFolder().toPath().resolve("arenas");

        if (!Files.exists(arenasPath)) {
            Files.createDirectory(arenasPath);
        }

        return arenasPath;
    }

    @Provides
    @Singleton
    @Named("messages")
    public YamlDocument provideLang(PaintballBattle plugin) throws IOException {
        return YamlDocument.create(new File(plugin.getDataFolder(), "messages.yml"),
                plugin.getResource("messages.yml"),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("lang-version")).build());
    }

    @Provides
    @Singleton
    @Named("config")
    public YamlDocument provideConfig(PaintballBattle plugin) throws IOException {
        return YamlDocument.create(new File(plugin.getDataFolder(), "config.yml"),
                plugin.getResource("config.yml"),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
    }

    @Provides
    @Singleton
    @Named("shop")
    public YamlDocument provideShop(PaintballBattle plugin) throws IOException {
        return YamlDocument.create(new File(plugin.getDataFolder(), "shop.yml"),
                plugin.getResource("shop.yml"),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("shop-version")).build());
    }

    @Provides
    @Singleton
    @Named("items")
    public YamlDocument provideItems(PaintballBattle plugin) throws IOException {
        return YamlDocument.create(new File(plugin.getDataFolder(), "items.yml"),
                plugin.getResource("items.yml"),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
    }

    @Provides
    @Singleton
    @Named("teams")
    public YamlDocument provideTeams(PaintballBattle plugin) throws IOException {
        return YamlDocument.create(new File(plugin.getDataFolder(), "teams.yml"),
                plugin.getResource("teams.yml"));
    }

    @Provides
    @Singleton
    @Named("signs")
    public YamlDocument provideSigns(PaintballBattle plugin) throws IOException {
        return YamlDocument.create(new File(plugin.getDataFolder(), "signs.yml"),
                plugin.getResource("signs.yml"));
    }

    @Provides
    @Singleton
    @Named("hats")
    public YamlDocument provideHats(PaintballBattle plugin) throws IOException {
        return YamlDocument.create(new File(plugin.getDataFolder(), "hats.yml"),
                plugin.getResource("hats.yml"));
    }

    @Provides
    @Singleton
    @Named("perks")
    public YamlDocument providePerks(PaintballBattle plugin) throws IOException {
        return YamlDocument.create(new File(plugin.getDataFolder(), "perks.yml"),
                plugin.getResource("perks.yml"));
    }

    @Singleton
    @Provides
    public MessageHandler messageHandlerProvider(PaintballBattle plugin, @Named("messages") YamlDocument lang) {
        return new MessageHandler(plugin, lang);
    }
}

