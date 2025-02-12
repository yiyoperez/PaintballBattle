package pb.ajneb97.tasks;

import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.ScoreboardManager;
import pb.ajneb97.utils.enums.GameState;
import pb.ajneb97.utils.enums.Messages;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreboardTask implements Runnable {

    private final GameManager gameManager;
    private final Cache<UUID, FastBoard> boardCache;
    private final MessageHandler messageHandler;
    private final ScoreboardManager scoreboardManager;

    public ScoreboardTask(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
        this.gameManager = scoreboardManager.getGameManager();
        this.boardCache = scoreboardManager.getBoardCache();
        this.messageHandler = scoreboardManager.getMessageHandler();
    }

    @Override
    public void run() {
        for (Map.Entry<UUID, FastBoard> entry : boardCache.get().entrySet()) {
            UUID uuid = entry.getKey();
            FastBoard board = entry.getValue();

            gameManager.getPlayerGame(uuid).ifPresent(game -> {
                if (game.getState() == GameState.ENDING) {
                    scoreboardManager.deleteScoreboard(uuid);
                    return;
                }

                board.updateTitle(Component.text(messageHandler.getMessage(Messages.GAME_SCOREBOARD_TITLE)));

                List<String> scoreboardBody = messageHandler.getRawStringList(Messages.GAME_SCOREBOARD_BODY.getPath());
                for (int i = 0; i < scoreboardBody.size(); i++) {
                    String message = scoreboardBody.get(i);

                    //TODO
                    //  REPLACE minimessage format, team score and stuff..

                    board.updateLine(i, Component.text(message));
                }
            });
        }
    }
}
