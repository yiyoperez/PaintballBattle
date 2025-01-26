package pb.ajneb97.injector.modules;

import pb.ajneb97.commons.loader.Loader;
import pb.ajneb97.injector.loaders.CommandsLoader;
import pb.ajneb97.injector.loaders.ItemsLoader;
import team.unnamed.inject.AbstractModule;

public final class LoadersModule extends AbstractModule {

    @Override
    protected void configure() {
        multibind(Loader.class)
                .asSet()
                .to(ItemsLoader.class)
                .to(CommandsLoader.class)
                .singleton();
    }
}
