package pb.ajneb97.listeners.customevents;

import pb.ajneb97.core.events.BaseEvent;
import pb.ajneb97.structures.game.Game;

/**
 * Called once game has fully ended.
 */
public class GameEndedEvent extends BaseEvent {

    private final Game game;

    public GameEndedEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
