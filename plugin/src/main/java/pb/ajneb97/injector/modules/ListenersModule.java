package pb.ajneb97.injector.modules;

import org.bukkit.event.Listener;
import pb.ajneb97.listeners.GameJoinListener;
import pb.ajneb97.listeners.GameLeaveListener;
import pb.ajneb97.listeners.GameListeners;
import pb.ajneb97.listeners.PreGameListeners;
import pb.ajneb97.listeners.PreJoinGameListener;
import pb.ajneb97.listeners.ShopListeners;
import pb.ajneb97.listeners.SignsListener;
import pb.ajneb97.managers.inventory.InventarioHats;
import team.unnamed.inject.AbstractModule;

public class ListenersModule extends AbstractModule {

    @Override
    protected void configure() {
        multibind(Listener.class)
                .asSet()
                .to(GameListeners.class)
                .to(PreGameListeners.class)
                .to(GameJoinListener.class)
                .to(GameLeaveListener.class)
                .to(PreJoinGameListener.class)

                .to(SignsListener.class)
                .to(ShopListeners.class)
                .to(InventarioHats.class)
                .singleton();
    }
}
