package pb.ajneb97.listeners.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import pb.ajneb97.core.events.BaseEvent;
import pb.ajneb97.structures.Game;

public class PreJoinGameEvent extends BaseEvent implements Cancellable {

    private boolean cancelled = false;
    private final Game game;
    private final Player player;

    public PreJoinGameEvent(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void setCancelled(boolean value) {
        this.cancelled = value;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
