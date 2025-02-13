package pb.ajneb97.tasks;

import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.managers.GameHandler;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.GameState;
import pb.ajneb97.utils.enums.Messages;

import java.util.HashSet;
import java.util.Set;

public class StartingGameTask implements Runnable {

    private final GameManager gameManager;
    private final GameHandler gameHandler;
    private final MessageHandler messageHandler;

    public StartingGameTask(GameManager gameManager, GameHandler gameHandler, MessageHandler messageHandler) {
        this.gameManager = gameManager;
        this.gameHandler = gameHandler;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        Set<Game> collect = new HashSet<>();
        for (Game game1 : gameManager.getGames()) {
            if (game1.isEnabled()) {
                if (game1.getState() == GameState.STARTING) {
                    collect.add(game1);
                }
            }
        }

        if (collect.isEmpty()) {
            gameHandler.stopWaitingTask();
            return;
        }

        for (Game game : collect) {// Is elapsed game is below 5 "seconds" send starting message.
            long elapsedTime = (game.getStartingTime() - System.currentTimeMillis()) / 1000;
            if (elapsedTime > 0) {
                game.notifyPlayers(messageHandler.getMessage(Messages.ARENA_STARTING_MESSAGE, new Placeholder("%time%", elapsedTime)));
                continue;
            }

            // Start the game if time has passed.
            Logger.info("Starting game.");
            gameHandler.startGame(game);
        }
    }
}
