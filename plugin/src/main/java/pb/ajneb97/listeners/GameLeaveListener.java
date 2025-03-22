package pb.ajneb97.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.listeners.customevents.GameLeaveEvent;
import pb.ajneb97.managers.controller.GameController;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

public class GameLeaveListener implements Listener {

    @Inject
    private GameController gameController;
    @Inject
    private MessageHandler messageHandler;

    @EventHandler
    public void onGameQuit(GameLeaveEvent event) {
        Game game = event.getGame();
        Player player = event.getPlayer();

        gameController.handlePlayerQuit(player, game);

        gameController.notifyPlayers(game, messageHandler.getMessage(Messages.PLAYER_LEAVE,
                new Placeholder("%player%", player.getName()),
                new Placeholder("%max_players%", game.getMaxPlayers()),
                new Placeholder("%current_players%", game.getCurrentPlayersSize())
        ));
    }
}
