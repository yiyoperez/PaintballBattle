package pb.ajneb97.injector.modules;

import pb.ajneb97.commons.service.Service;
import pb.ajneb97.injector.services.GameService;
import pb.ajneb97.injector.services.ListenersService;
import pb.ajneb97.injector.services.PluginService;
import pb.ajneb97.injector.services.SignsService;
import team.unnamed.inject.AbstractModule;

public final class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Service.class).named("plugin-service").to(PluginService.class).singleton();

        multibind(Service.class)
                .asSet()
                .to(GameService.class)
                .to(SignsService.class)
                .to(ListenersService.class)
                .singleton();
    }
}
