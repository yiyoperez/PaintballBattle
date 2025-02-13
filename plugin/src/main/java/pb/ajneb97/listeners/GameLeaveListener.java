package pb.ajneb97.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pb.ajneb97.listeners.customevents.GameLeaveEvent;
import pb.ajneb97.managers.GameHandler;
import pb.ajneb97.structures.game.Game;
import team.unnamed.inject.Inject;

public class GameLeaveListener implements Listener {

    @Inject
    private GameHandler gameHandler;

    @EventHandler
    public void onGameQuit(GameLeaveEvent event) {
        Game game = event.getGame();
        Player player = event.getPlayer();

        gameHandler.handlePlayerQuit(player, game);
    }
}
