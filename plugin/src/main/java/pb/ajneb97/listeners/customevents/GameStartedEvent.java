package pb.ajneb97.listeners.customevents;

import pb.ajneb97.core.events.BaseEvent;
import pb.ajneb97.structures.game.Game;

public class GameStartedEvent extends BaseEvent {

    private final Game game;

    public GameStartedEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
