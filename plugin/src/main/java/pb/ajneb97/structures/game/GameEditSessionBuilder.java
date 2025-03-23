package pb.ajneb97.structures.game;

import org.bukkit.Location;
import pb.ajneb97.structures.Team;
import pb.ajneb97.utils.enums.GameState;

public class GameEditSessionBuilder {

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
    private int maxTime;
    private int startingLives;

    public GameEditSessionBuilder(String name) {
        this.name = name;
        this.enabled = false;
        this.minPlayers = 4;
        this.maxPlayers = 16;
        this.state = GameState.DISABLED;
        this.maxTime = 360;
        this.startingLives = 100;
    }

    public GameEditSessionBuilder(Game game) {
        this.name = game.getName();
        this.enabled = game.isEnabled();
        this.minPlayers = game.getMinPlayers();
        this.maxPlayers = game.getMaxPlayers();
        this.firstTeam = game.getFirstTeam();
        this.secondTeam = game.getSecondTeam();
        this.state = game.getState();
        this.lobby = game.getLobby();
        this.pointOne = game.getPointOne();
        this.pointTwo = game.getPointTwo();
        this.maxTime = game.getMaxTime();
        this.startingLives = game.getStartingLives();
    }

    public GameEditSessionBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public GameEditSessionBuilder setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public GameEditSessionBuilder setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public GameEditSessionBuilder setFirstTeam(Team firstTeam) {
        this.firstTeam = firstTeam;
        return this;
    }

    public GameEditSessionBuilder setSecondTeam(Team secondTeam) {
        this.secondTeam = secondTeam;
        return this;
    }

    public GameEditSessionBuilder setState(GameState state) {
        this.state = state;
        return this;
    }

    public GameEditSessionBuilder setLobby(Location lobby) {
        this.lobby = lobby;
        return this;
    }

    public GameEditSessionBuilder setPointOne(Location pointOne) {
        this.pointOne = pointOne;
        return this;
    }

    public GameEditSessionBuilder setPointTwo(Location pointTwo) {
        this.pointTwo = pointTwo;
        return this;
    }

    public GameEditSessionBuilder setMaxTime(int maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public GameEditSessionBuilder setStartingLives(int startingLives) {
        this.startingLives = startingLives;
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Team getFirstTeam() {
        return firstTeam;
    }

    public Team getSecondTeam() {
        return secondTeam;
    }

    public GameState getState() {
        return state;
    }

    public Location getLobby() {
        return lobby;
    }

    public Location getPointOne() {
        return pointOne;
    }

    public Location getPointTwo() {
        return pointTwo;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public int getStartingLives() {
        return startingLives;
    }

    public Game build() {
        Game game = new Game(name);
        game.setEnabled(enabled);
        game.setMinPlayers(minPlayers);
        game.setMaxPlayers(maxPlayers);
        game.setFirstTeam(firstTeam);
        game.setSecondTeam(secondTeam);
        game.setState(state);
        game.setLobby(lobby);
        game.setPointOne(pointOne);
        game.setPointTwo(pointTwo);
        game.setMaxTime(maxTime);
        game.setStartingLives(startingLives);

        return game;
    }
}