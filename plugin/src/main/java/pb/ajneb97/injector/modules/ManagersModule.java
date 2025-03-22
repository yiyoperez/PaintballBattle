package pb.ajneb97.injector.modules;

import pb.ajneb97.PaintballAPI;
import pb.ajneb97.inventories.EditInventory;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.ScoreboardManager;
import pb.ajneb97.managers.ShopManager;
import pb.ajneb97.managers.SignManager;
import pb.ajneb97.managers.controller.GameController;
import team.unnamed.inject.AbstractModule;

public final class ManagersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GameManager.class).singleton();
        bind(GameController.class).singleton();
        bind(SignManager.class).singleton();
        bind(ShopManager.class).singleton();
        bind(PaintballAPI.class).singleton();
        bind(ScoreboardManager.class).singleton();

        bind(EditInventory.class).singleton();
    }
}
