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
            messageHandler.sendMessage(player, "arenaDisabledError");
            event.setCancelled(true);
            return;
        }

        if (game.hasStarted()) {
            messageHandler.sendMessage(player, "arenaAlreadyStarted");
            event.setCancelled(true);
            return;
        }

        if (gameManager.isPlaying(player)) {
            messageHandler.sendMessage(player, "alreadyInArena");
            event.setCancelled(true);
            return;
        }

        if (!gameManager.isGameAvailable(game)) {
            messageHandler.sendManualMessage(player, "Game is not currently available." + game.getState().name());
            event.setCancelled(true);
            return;
        }

        if (game.isFull()) {
            messageHandler.sendMessage(player, "arenaIsFull");
            event.setCancelled(true);
            return;
        }

        //TODO empty_inventory_to_join
//        if (!UtilidadesOtros.pasaConfigInventario(player, config)) {
//            messageHandler.sendMessage(player, "arenaDisabledError");
//            player.sendMessage(MessageUtils.translateColor(messages.getString("errorClearInventory")));
//            event.setCancelled(true);
//        }

        new GameJoinEvent(game, player).call();
    }
}
