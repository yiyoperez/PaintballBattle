package pb.ajneb97.listeners.customevents;

import org.bukkit.entity.Player;
import pb.ajneb97.core.events.BaseEvent;

public class PlayerGameDeathEvent extends BaseEvent {

    private final Player player;

    public PlayerGameDeathEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
