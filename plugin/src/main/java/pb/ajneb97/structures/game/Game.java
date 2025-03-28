package pb.ajneb97.structures.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.structures.Team;
import pb.ajneb97.utils.LocationUtils;
import pb.ajneb97.utils.enums.GameState;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Game implements ConfigurationSerializable {

    private final String name;
    private boolean enabled;

    private int minPlayers;
    private int maxPlayers;

    private Team firstTeam;
    private Team secondTeam;
    private GameState state;

    private Location lobby;
    private Location pointOne;
    private Location pointTwo;

    private long startingTime;
    private int maxTime;
    private int startingLives;

    private final Set<UUID> currentPlayers = new HashSet<>();

    public Game(String name) {
        this.name = name;
        this.enabled = false;

        this.minPlayers = 4;
        this.maxPlayers = 16;
        this.state = GameState.DISABLED;

        this.maxTime = 360;
        this.startingLives = 100;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("NAME", getName());
        map.put("ENABLED", enabled);
        map.put("MIN_PLAYERS", getMinPlayers());
        map.put("MAX_PLAYERS", getMaxPlayers());
        map.put("MAX_TIME", maxTime);
        map.put("STATE", enabled ? GameState.WAITING.name() : GameState.DISABLED.name());
        map.put("STARTING_LIVES", startingLives);
        map.put("LOBBY", LocationUtils.serialize(getLobby()));
        map.put("POINT_ONE", LocationUtils.serialize(pointOne));
        map.put("POINT_TWO", LocationUtils.serialize(pointTwo));

        return map;
    }

    public Game(Map<String, Object> map) {
        this.name = (String) map.get("NAME");
        this.enabled = (Boolean) map.getOrDefault("ENABLED", false);

        this.minPlayers = (Integer) map.getOrDefault("MIN_PLAYERS", 0);
        this.maxPlayers = (Integer) map.getOrDefault("MAX_PLAYERS", 0);

        this.maxTime = (Integer) map.getOrDefault("MAX_TIME", 360);
        this.state = GameState.valueOf((String) map.get("STATE"));
        this.startingLives = (Integer) map.getOrDefault("STARTING_LIVES", 100);

        // Locations
        if (map.containsKey("LOBBY")) {
            this.lobby = LocationUtils.deserialize((String) map.get("LOBBY"));
        }
        if (map.containsKey("POINT_ONE")) {
            this.pointOne = LocationUtils.deserialize((String) map.get("POINT_ONE"));
        }
        if (map.containsKey("POINT_TWO")) {
            this.pointTwo = LocationUtils.deserialize((String) map.get("POINT_TWO"));
        }
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.state = enabled ? GameState.WAITING : GameState.DISABLED;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Team getFirstTeam() {
        return firstTeam;
    }

    public void setFirstTeam(Team firstTeam) {
        this.firstTeam = firstTeam;
    }

    public Team getSecondTeam() {
        return secondTeam;
    }

    public void setSecondTeam(Team secondTeam) {
        this.secondTeam = secondTeam;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
        Logger.info("Game state has been set to " + state.name());
    }

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }

    public boolean hasLobby() {
        return lobby != null;
    }

    public Location getPointOne() {
        return pointOne;
    }

    public void setPointOne(Location pointOne) {
        this.pointOne = pointOne;
    }

    public Location getPointTwo() {
        return pointTwo;
    }

    public void setPointTwo(Location pointTwo) {
        this.pointTwo = pointTwo;
    }

    public long getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(long startingTime) {
        this.startingTime = startingTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public int getStartingLives() {
        return startingLives;
    }

    public void setStartingLives(int startingLives) {
        this.startingLives = startingLives;
    }

    public void addPlayer(Player player) {
        currentPlayers.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        currentPlayers.removeIf((uuid) -> contains(player));
    }

    public boolean contains(Player player) {
        return contains(player.getUniqueId());
    }

    public boolean contains(UUID uuid) {
        return currentPlayers.contains(uuid);
    }

    public Set<UUID> getCurrentPlayersUUID() {
        return currentPlayers;
    }

    public Set<Player> getCurrentPlayers() {
        Set<Player> set = new HashSet<>();
        for (UUID currentPlayer : currentPlayers) {
            Player player = Bukkit.getPlayer(currentPlayer);
            set.add(player);
        }
        return set;
    }

    public int getCurrentPlayersSize() {
        return this.currentPlayers.size();
    }

    public boolean hasStarted() {
        return this.state == GameState.PLAYING;
    }

    public boolean isFull() {
        return getCurrentPlayersSize() == this.maxPlayers;
    }
}
