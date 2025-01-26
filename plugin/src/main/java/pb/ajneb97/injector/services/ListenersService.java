package pb.ajneb97.injector.services;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commons.service.Service;
import pb.ajneb97.core.logger.Logger;
import team.unnamed.inject.Inject;

import java.util.Set;

public final class ListenersService implements Service {

    @Inject
    private PaintballBattle plugin;
    @Inject
    private Set<Listener> listenerSet;

    @Override
    public void start() {
        Logger.info("Starting listeners service.");
        listenerSet.forEach(listener -> plugin.addListeners(listener));
    }

    @Override
    public void finish() {
        HandlerList.unregisterAll(plugin);
    }
}
