package pb.ajneb97.listeners.customevents;

import org.bukkit.entity.Player;
import pb.ajneb97.core.events.BaseEvent;

public class GameDeathEvent extends BaseEvent {

    private final Player dead;
    private final Player killer;

    public GameDeathEvent(Player dead, Player killer) {
        this.dead = dead;
        this.killer = killer;
    }

    public Player getDead() {
        return dead;
    }

    public Player getKiller() {
        return killer;
    }
}
