package pb.ajneb97.structures;

import org.bukkit.entity.Player;
import pb.ajneb97.structures.game.SavedElements;


public class PaintballPlayer {

    private Player player;

    private int coins;
    private int kills;
    private int deaths;

    private String lastKilledBy;
    private long recentDeathMillis;

    private String teamChoice;
    private final SavedElements savedElements;

    public PaintballPlayer(Player player) {
        this.player = player;

        this.deaths = 0;
        this.kills = 0;
        this.coins = 0;

        this.lastKilledBy = null;
        this.recentDeathMillis = -1;

        this.savedElements = new SavedElements(player);
    }

    public Player getPlayer() {
        return player;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void increaseKills() {
        this.kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void increaseDeaths() {
        this.deaths++;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public String getLastKilledBy() {
        return lastKilledBy;
    }

    public void setLastKilledBy(String lastKilledBy) {
        this.lastKilledBy = lastKilledBy;
    }

    public long getRecentDeathMillis() {
        return recentDeathMillis;
    }

    public void setRecentDeathMillis(long recentDeathMillis) {
        this.recentDeathMillis = recentDeathMillis;
    }

    public String getTeamChoice() {
        return teamChoice;
    }

    public void setTeamChoice(String teamChoice) {
        this.teamChoice = teamChoice;
    }

    public SavedElements getSavedElements() {
        return savedElements;
    }
}
