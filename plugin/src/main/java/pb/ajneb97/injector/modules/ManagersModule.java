package pb.ajneb97.injector.modules;

import pb.ajneb97.PaintballAPI;
import pb.ajneb97.managers.EditManager;
import pb.ajneb97.managers.GameHandler;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.ScoreboardManager;
import pb.ajneb97.managers.ShopManager;
import pb.ajneb97.managers.SignManager;
import team.unnamed.inject.AbstractModule;

public final class ManagersModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(GameManager.class).singleton();
        bind(GameHandler.class).singleton();
        bind(SignManager.class).singleton();
        bind(ShopManager.class).singleton();
        bind(EditManager.class).singleton();
        bind(PaintballAPI.class).singleton();
        bind(ScoreboardManager.class).singleton();
    }

}
