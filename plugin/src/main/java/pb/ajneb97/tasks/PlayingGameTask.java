package pb.ajneb97.tasks;

import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.controller.GameController;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.GameState;

import java.util.HashSet;
import java.util.Set;

public class PlayingGameTask implements Runnable {

    private final GameManager gameManager;
    private final GameController gameController;

    public PlayingGameTask(GameManager gameManager, GameController gameController) {
        this.gameManager = gameManager;
        this.gameController = gameController;
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
            gameController.stopPlayingTask();
            return;
        }

        for (Game game : collect) {
            if (game.getFirstTeam().getLives() <= 0 || game.getSecondTeam().getLives() <= 0) {
                Logger.info("Starting ending phase due no lives left.");
                gameController.initEndingPhase(game);
                return;
            }

            long remainingTime = (game.getStartingTime() - System.currentTimeMillis()) / 1000;
            if (remainingTime <= 0) {
                Logger.info("Starting ending phase due time limit.");
                gameController.initEndingPhase(game);
            }
        }
    }
}
