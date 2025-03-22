package pb.ajneb97.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.listeners.customevents.GameJoinEvent;
import pb.ajneb97.listeners.customevents.PreJoinGameEvent;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

public class PreJoinGameListener implements Listener {

    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;

    @EventHandler
    public void preJoinEvent(PreJoinGameEvent event) {
        Game game = event.getGame();
        Player player = event.getPlayer();
        if (!game.isEnabled()) {
            messageHandler.sendMessage(player, Messages.ARENA_DISABLED_ERROR);
            event.setCancelled(true);
            return;
        }

        if (game.hasStarted()) {
            messageHandler.sendMessage(player, Messages.ARENA_ALREADY_STARTED);
            event.setCancelled(true);
            return;
        }

        if (gameManager.isPlaying(player)) {
            messageHandler.sendMessage(player, Messages.ALREADY_IN_ARENA);
            event.setCancelled(true);
            return;
        }

        if (!gameManager.isGameAvailable(game)) {
            messageHandler.sendManualMessage(player, "Game is not currently available. " + game.getState().name());
            event.setCancelled(true);
            return;
        }

        if (game.isFull()) {
            messageHandler.sendMessage(player, Messages.ARENA_IS_FULL);
            event.setCancelled(true);
            return;
        }

        new GameJoinEvent(game, player).call();
    }
}
