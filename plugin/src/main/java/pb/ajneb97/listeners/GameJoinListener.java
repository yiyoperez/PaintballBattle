package pb.ajneb97.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pb.ajneb97.listeners.customevents.GameJoinEvent;
import pb.ajneb97.managers.GameHandler;
import pb.ajneb97.structures.game.Game;
import team.unnamed.inject.Inject;

public class GameJoinListener implements Listener {

    @Inject
    private GameHandler gameHandler;

    @EventHandler
    public void onGameJoin(GameJoinEvent event) {
        Game game = event.getGame();
        Player player = event.getPlayer();

        gameHandler.handlePlayerJoin(player, game);
    }
}
