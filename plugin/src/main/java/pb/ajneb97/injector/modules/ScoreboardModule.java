package pb.ajneb97.injector.modules;

import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.core.logger.Logger;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

public class ScoreboardModule extends AbstractModule {

    @Provides
    @Singleton
    public ScoreboardLibrary provideScoreboard(PaintballBattle plugin) {
        ScoreboardLibrary scoreboardLibrary;
        try {
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(plugin);
        } catch (NoPacketAdapterAvailableException e) {
            scoreboardLibrary = new NoopScoreboardLibrary();
            Logger.info("No scoreboard packet adapter available!");
        }
        return scoreboardLibrary;
    }
}
