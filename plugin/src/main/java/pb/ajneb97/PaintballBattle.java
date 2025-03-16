package pb.ajneb97;

import pb.ajneb97.commons.service.Service;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.SPlugin;
import pb.ajneb97.injector.MainModule;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Injector;
import team.unnamed.inject.Named;

public final class PaintballBattle extends SPlugin {

    @Inject
    @Named("plugin-service")
    private Service service;

    @Inject
    private PaintballAPI API;

    public void onEnable() {
        preEnable();
        try {
            Injector.create(new MainModule(this))
                    .injectMembers(this);

            service.start();
        } catch (Exception e) {
            this.failedEnable();
            Logger.info(e.getMessage(), Logger.LogType.ERROR);
        } finally {
            postEnable();
        }
    }

    @Override
    public void onDisable() {
        preDisable();
        try {
            service.finish();
        } catch (Exception e) {
            Logger.info(e.getMessage(), Logger.LogType.ERROR);
        } finally {
            postDisable();
        }
    }

    public PaintballAPI getAPI() {
        return API;
    }

}
