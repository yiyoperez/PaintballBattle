package pb.ajneb97.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.listeners.customevents.GameStartedEvent;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.controller.GameController;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

public class GameStartListener implements Listener {

    @Inject
    private GameManager gameManager;
    @Inject
    private GameController gameController;
    @Inject
    private MessageHandler messageHandler;

    @EventHandler
    public void onGameStart(GameStartedEvent event) {
        Game game = event.getGame();
        //TODO: Improve sound thing.
        //startGameSound

        //TODO: Missing give items and extra lives from perks.

        // TODO should give player hats & perk items
        //darItems(partida, plugin.getConfigDocument(), plugin.getShopDocument(), messages);
        // TODO changes team lives if players have perks which gives them more lives...
        //setVidas(partida, plugin.getShopDocument());

        gameController.notifyPlayers(game, messageHandler.getMessage(Messages.GAME_STARTED));
        gameController.notifyPlayers(game, (player) ->
                player.sendMessage(messageHandler.getMessage(
                        Messages.TEAM_INFORMATION,
                        new Placeholder("%team%", gameManager.getPlayerTeam(player).getName()))));
    }
}
