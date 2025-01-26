package pb.ajneb97.structures;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import pb.ajneb97.utils.enums.TeamReward;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class Team {

    private final String name;

    private int lives;
    private int kills;
    private int deaths;
    private TeamReward reward;
    private Location spawnLocation;

    private final Set<UUID> players;

    public Team(String name, int lives) {
        this.name = name;
        this.lives = lives;
        this.players = new HashSet<>();
        this.kills = 0;
        this.deaths = 0;
        this.reward = TeamReward.UNKNOWN;
    }

    public String getName() {
        return this.name;
    }

    public int getLives() {
        return this.lives;
    }

    public void decreaseLives(int amount) {
        this.lives = this.lives - amount;
    }

    public void increaseLives(int amount) {
        this.lives = this.lives + amount;
    }

    public void setLives(int amount) {
        this.lives = amount;
    }

    public boolean contains(Player player) {
        return players.contains(player.getUniqueId());
    }

    public boolean addPlayer(Player player) {
        return players.add(player.getUniqueId());
    }

    public boolean addPlayer(UUID uuid) {
        return players.add(uuid);
    }

    public boolean removePlayer(Player player) {
        return players.removeIf(uuid -> player.getUniqueId().equals(uuid));
    }

    public boolean removePlayer(UUID uuid) {
        return players.removeIf(unique -> unique.equals(uuid));
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public int getPlayersSize() {
        return this.players.size();
    }

    public int getKills() {
        return kills;
    }

    public void increaseKills() {
        this.kills++;
    }

    public void setKills(int kills) {
        this.kills = kills;
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

    public TeamReward getReward() {
        return reward;
    }

    public void setReward(TeamReward reward) {
        this.reward = reward;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
