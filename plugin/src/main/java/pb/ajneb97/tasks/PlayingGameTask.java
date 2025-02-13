package pb.ajneb97.tasks;

import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.managers.GameHandler;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.GameState;

import java.util.HashSet;
import java.util.Set;

public class PlayingGameTask implements Runnable {

    private final GameManager gameManager;
    private final GameHandler gameHandler;

    public PlayingGameTask(GameManager gameManager, GameHandler gameHandler) {
        this.gameManager = gameManager;
        this.gameHandler = gameHandler;
    }

    @Override
    public void run() {
        Set<Game> collect = new HashSet<>();
        for (Game game : gameManager.getGames()) {
            if (game.isEnabled()) {
                if (game.getState() == GameState.PLAYING) {
                    collect.add(game);
                }
            }
        }

        if (collect.isEmpty()) {
            gameHandler.stopPlayingTask();
            return;
        }

        for (Game game : collect) {
            long remainingTime = (game.getStartingTime() - System.currentTimeMillis()) / 1000;
            Logger.info("Remaining time is " + remainingTime);
            if (remainingTime <= 0) {
                Logger.info("Starting ending phase due time limit.");
                gameHandler.initEndingPhase(game);
            }
        }
    }
}
