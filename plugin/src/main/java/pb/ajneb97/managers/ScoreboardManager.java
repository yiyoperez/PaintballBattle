package pb.ajneb97.managers;

import fr.mrmicky.fastboard.adventure.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.tasks.ScoreboardTask;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.UUID;

public class ScoreboardManager {

    @Inject
    private PaintballBattle plugin;
    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;
    @Inject
    @Named("board-cache")
    private Cache<UUID, FastBoard> boardCache;

    private int taskID;

    public void scheduleTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (!scheduler.isCurrentlyRunning(taskID)) {
            Logger.info("Starting scoreboard task");
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
        boardCache.add(player.getUniqueId(), new FastBoard(player));
    }

    public void deleteScoreboard(Player player) {
        deleteScoreboard(player.getUniqueId());
    }

    public void deleteScoreboard(UUID uuid) {
        boardCache.find(uuid).ifPresent(fastBoard -> {
            fastBoard.delete();
            boardCache.remove(uuid);
        });
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public Cache<UUID, FastBoard> getBoardCache() {
        return boardCache;
    }

    public int getTaskID() {
        return taskID;
    }
}
