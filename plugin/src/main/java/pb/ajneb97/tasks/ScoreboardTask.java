package pb.ajneb97.tasks;

import pb.ajneb97.managers.ScoreboardManager;

public class ScoreboardTask implements Runnable {

    private final ScoreboardManager scoreboardManager;

    public ScoreboardTask(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public void run() {
        scoreboardManager.getBoardCache()
                .get()
                .keySet()
                .forEach(scoreboardManager::updateScoreboard);
    }
}
