package pb.ajneb97.structures.scoreboard;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class ScoreboardHelper {

    private final Sidebar sidebar;

    public ScoreboardHelper(ScoreboardLibrary scoreboardLibrary) {
        this.sidebar = scoreboardLibrary.createSidebar(Sidebar.MAX_LINES);
    }

    public void setTitle(Component title) {
        sidebar.title(title);
    }

    public void setBody(List<Component> lines) {
        for (int i = 0; i < lines.size(); i++) {
            sidebar.line(i, lines.get(i));
        }
    }

    public void addPlayer(Player player) {
        sidebar.addPlayer(player);
    }

    public void addPlayers(Collection<Player> players) {
        sidebar.addPlayers(players);
    }

    public void removePlayer(Player player) {
        sidebar.removePlayer(player);
    }

    public void removePlayers(Collection<Player> players) {
        sidebar.removePlayers(players);
    }

}
