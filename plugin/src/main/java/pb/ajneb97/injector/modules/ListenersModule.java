package pb.ajneb97.injector.modules;

import org.bukkit.event.Listener;
import pb.ajneb97.inventories.InventarioHats;
import pb.ajneb97.listeners.*;
import team.unnamed.inject.AbstractModule;

public final class ListenersModule extends AbstractModule {

    @Override
    protected void configure() {
        multibind(Listener.class)
                .asSet()
                .to(GameListeners.class)
                .to(GameJoinListener.class)
                .to(GameLeaveListener.class)

                .to(PreJoinGameListener.class)
                .to(GameStartListener.class)
                .to(GameEndedListener.class)
                .to(GameDeathListener.class)
                .to(GameEndingListener.class)

                .to(SignsListener.class)
                .to(ShopListeners.class)
                .to(InventarioHats.class)
                .singleton();
    }
}
