package pb.ajneb97.listeners.customevents;

import pb.ajneb97.core.events.BaseEvent;
import pb.ajneb97.structures.game.Game;

/**
 * Called once game start to end and has a grace period.
 */
public class GameEndingEvent extends BaseEvent {

    private final Game game;

    public GameEndingEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
