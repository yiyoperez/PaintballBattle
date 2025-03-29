package pb.ajneb97.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.core.utils.message.MessageBuilder;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.listeners.customevents.GameEndingEvent;
import pb.ajneb97.managers.controller.GameController;
import pb.ajneb97.structures.PaintballPlayer;
import pb.ajneb97.structures.Team;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameEndingListener implements Listener {

    @Inject
    private GameController gameController;
    @Inject
    private MessageHandler messageHandler;
    @Inject
    @Named("player-cache")
    private Cache<UUID, PaintballPlayer> playerCache;

    @EventHandler
    public void onGameEnding(GameEndingEvent event) {
        Game game = event.getGame();

        Team firstTeam = game.getFirstTeam();
        Team secondTeam = game.getSecondTeam();

        // Initialize default top names and kills.
        String[] tops = {messageHandler.getRawMessage(Messages.TOP_KILLS_NONE), messageHandler.getRawMessage(Messages.TOP_KILLS_NONE), messageHandler.getRawMessage(Messages.TOP_KILLS_NONE)};
        int[] topKills = {0, 0, 0};

        List<PaintballPlayer> sortedPlayerKills = new ArrayList<>();
        game.getCurrentPlayersUUID().forEach(uuid -> playerCache.find(uuid).ifPresent(sortedPlayerKills::add));

        // Fill the top arrays with available players.
        for (int i = 0; i < Math.min(sortedPlayerKills.size(), 3); i++) {
            tops[i] = sortedPlayerKills.get(i).getPlayer().getName();
            topKills[i] = sortedPlayerKills.get(i).getKills();
        }

        // Assign results to top variables.
        String top1 = tops[0], top2 = tops[1], top3 = tops[2];
        int top1Kills = topKills[0], top2Kills = topKills[1], top3Kills = topKills[2];

        Team winner = gameController.getWinner(game);
        List<Placeholder> placeholderList = new ArrayList<>();
        placeholderList.add(new Placeholder("%status_message%", messageHandler.getParsedMessage((winner == null ? Messages.GAME_FINISHED_TIE_STATUS : Messages.GAME_FINISHED_WINNER_STATUS), new Placeholder("%winner_team%", winner.getName()))));
        placeholderList.add(new Placeholder("%team1%", firstTeam.getName()));
        placeholderList.add(new Placeholder("%team2%", secondTeam.getName()));
        placeholderList.add(new Placeholder("%kills_team1%", firstTeam.getKills()));
        placeholderList.add(new Placeholder("%kills_team2%", secondTeam.getKills() + ""));
        placeholderList.add(new Placeholder("%player1%", top1));
        placeholderList.add(new Placeholder("%player2%", top2));
        placeholderList.add(new Placeholder("%player3%", top3));
        placeholderList.add(new Placeholder("%kills_player1%", top1Kills));
        placeholderList.add(new Placeholder("%kills_player2%", top2Kills));
        placeholderList.add(new Placeholder("%kills_player3%", top3Kills));
        //*TODO: Replace player kills*//*
        //placeholderList.add(new Placeholder("%kills_player%", 0));

        new MessageBuilder(messageHandler)
                .toPlayerSet(game.getCurrentPlayers())
                .withPlaceholders(placeholderList)
                .withMessage(Messages.GAME_FINISHED)
                .sendList();
    }
}
