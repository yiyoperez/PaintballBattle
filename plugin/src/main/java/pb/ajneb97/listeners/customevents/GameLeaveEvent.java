package pb.ajneb97.listeners.customevents;

import org.bukkit.entity.Player;
import pb.ajneb97.core.events.BaseEvent;
import pb.ajneb97.structures.Game;

public class GameLeaveEvent extends BaseEvent {

    private final Game game;
    private final Player player;

    public GameLeaveEvent(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }
}
