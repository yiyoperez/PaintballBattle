package pb.ajneb97.injector;

import pb.ajneb97.PaintballBattle;
import pb.ajneb97.injector.modules.CacheModule;
import pb.ajneb97.injector.modules.CommandsModule;
import pb.ajneb97.injector.modules.FilesModule;
import pb.ajneb97.injector.modules.ListenersModule;
import pb.ajneb97.injector.modules.LoadersModule;
import pb.ajneb97.injector.modules.ManagersModule;
import pb.ajneb97.injector.modules.ScoreboardModule;
import pb.ajneb97.injector.modules.ServicesModule;
import team.unnamed.inject.AbstractModule;

public final class MainModule extends AbstractModule {

    private final PaintballBattle plugin;

    public MainModule(PaintballBattle plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(PaintballBattle.class).toInstance(this.plugin);

        install(new FilesModule());
        install(new CacheModule());

        install(new ScoreboardModule());

        install(new ManagersModule());

        install(new CommandsModule());
        install(new ListenersModule());

        install(new LoadersModule());
        install(new ServicesModule());
    }
}
