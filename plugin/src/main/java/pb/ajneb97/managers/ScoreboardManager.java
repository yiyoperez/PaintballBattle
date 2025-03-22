package pb.ajneb97.managers;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.message.MessageBuilder;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.structures.scoreboard.ScoreboardHelper;
import pb.ajneb97.tasks.ScoreboardTask;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreboardManager {

    @Inject
    private PaintballBattle plugin;
    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;
    @Inject
    private ScoreboardLibrary scoreboardLibrary;
    @Inject
    @Named("board-cache")
    private Cache<UUID, ScoreboardHelper> boardCache;

    private int taskID;

    //TODO: !!!!!!!!!!!!!! READ MF
    // Scoreboard system is currently completely broken, needs to create scoreboard per game/arena,
    // and update it from a scheduler or from event calls, such as kills, deaths, game state.

    public void scheduleTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (!scheduler.isCurrentlyRunning(taskID)) {
            taskID = scheduler.scheduleSyncRepeatingTask(plugin,
                    new ScoreboardTask(this), 0L, 20L);
        }
    }

    public void stopTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (scheduler.isCurrentlyRunning(taskID)) {
            scheduler.cancelTask(taskID);
        }
    }

    public void createScoreboard(Player player) {
        boardCache.add(player.getUniqueId(), new ScoreboardHelper(scoreboardLibrary));

        updateScoreboard(player.getUniqueId());
    }

    public ScoreboardHelper updateScoreboard(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null; // Player is offline, return early
        }

        // Find or create the ScoreboardHelper for the player
        ScoreboardHelper helper = boardCache.find(uuid)
                .orElseGet(() -> {
                    Logger.info("Returning new helper.");
                    ScoreboardHelper newHelper = new ScoreboardHelper(scoreboardLibrary);
                    boardCache.add(uuid, newHelper);
                    return newHelper;
                });

        // Update the scoreboard with the latest data
        helper.setTitle(messageHandler.getComponent(Messages.GAME_SCOREBOARD_TITLE));

        List<Placeholder> placeholderList = new ArrayList<>();
        gameManager.getPlayerGame(uuid).ifPresent(game -> {
            placeholderList.add(new Placeholder("%arena%", game.getName()));
            placeholderList.add(new Placeholder("%team_1%", game.getFirstTeam().getName()));
            placeholderList.add(new Placeholder("%team_1_lives%", game.getFirstTeam().getLives()));
            placeholderList.add(new Placeholder("%team_2%", game.getSecondTeam().getName()));
            placeholderList.add(new Placeholder("%team_2_lives%", game.getSecondTeam().getLives()));
            placeholderList.add(new Placeholder("%status%", game.getState().name()));
            placeholderList.add(new Placeholder("%max_players%", game.getMaxPlayers()));
            placeholderList.add(new Placeholder("%current_players%", game.getCurrentPlayersSize()));
        });

        List<Component> componentList = new MessageBuilder(messageHandler)
                .withMessage(Messages.GAME_SCOREBOARD_BODY)
                .withPlaceholders(placeholderList)
                .buildComponentList();

        helper.setBody(componentList);
        helper.addPlayer(player);

        return helper;
    }

    public void deleteScoreboard(Player player) {
        boardCache.find(player.getUniqueId()).ifPresent(helper -> {
            helper.removePlayer(player);
            boardCache.remove(player.getUniqueId());
        });
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public Cache<UUID, ScoreboardHelper> getBoardCache() {
        return boardCache;
    }

    public ScoreboardLibrary getScoreboardLibrary() {
        return scoreboardLibrary;
    }

    public int getTaskID() {
        return taskID;
    }
}
