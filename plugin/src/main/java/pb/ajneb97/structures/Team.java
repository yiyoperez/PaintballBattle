package pb.ajneb97.structures;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class Team {

    private final String name;
    private final Set<UUID> players;

    private int lives;
    private int kills;
    private int deaths;
    private Location spawnLocation;

    public Team(String name) {
        this.name = name;
        this.players = new HashSet<>();

        this.lives = 0;
        this.kills = 0;
        this.deaths = 0;
    }

    public Team(String name, int lives) {
        this(name);
        this.lives = lives;
    }

    public String getName() {
        return this.name;
    }

    public int getLives() {
        return this.lives;
    }

    public void decreaseLives() {
        decreaseLives(1);
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
        return contains(player.getUniqueId());
    }

    public boolean contains(UUID uuid) {
        return players.contains(uuid);
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

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
