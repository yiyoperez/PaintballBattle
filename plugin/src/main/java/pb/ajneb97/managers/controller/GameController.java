package pb.ajneb97.managers.controller;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.listeners.customevents.GameDeathEvent;
import pb.ajneb97.listeners.customevents.GameEndedEvent;
import pb.ajneb97.listeners.customevents.GameEndingEvent;
import pb.ajneb97.listeners.customevents.GameStartedEvent;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.ScoreboardManager;
import pb.ajneb97.structures.PaintballPlayer;
import pb.ajneb97.structures.Team;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.structures.game.GameItem;
import pb.ajneb97.tasks.PlayingGameTask;
import pb.ajneb97.tasks.StartingGameTask;
import pb.ajneb97.utils.LocationUtils;
import pb.ajneb97.utils.enums.GameState;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public final class GameController {

    @Inject
    private PaintballBattle plugin;
    @Inject
    @Named("config")
    private YamlDocument config;

    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;
    @Inject
    private ScoreboardManager scoreboardManager;

    @Inject
    @Named("item-cache")
    private Cache<String, GameItem> itemCache;
    @Inject
    @Named("player-cache")
    private Cache<UUID, PaintballPlayer> playerCache;

    //CREATE OWN CLASS TO MANAGE TASKS.
    private int waitingTaskID;
    private int playingTaskID;

    public void initStartingPhase(Game game) {
        // Game already been set as starting.
        if (game.getState() == GameState.STARTING) return;

        if (config.getBoolean("BROADCAST.ENABLED")) {
            List<String> worlds = config.getStringList("BROADCAST.DENIED_WORLDS");
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (!worlds.contains(player.getWorld().getName())) {
                    player.sendMessage(messageHandler.getMessage(Messages.ARENA_STARTING_BROADCAST, new Placeholder("%arena%", game.getName())));
                }
            });
        }

        game.setState(GameState.STARTING);

        int time = config.getInt("TIMES.STARTING");
        game.setStartingTime(System.currentTimeMillis() + time * 1000L);

        scheduleWaitingTask();
    }

    public void scheduleWaitingTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (!scheduler.isCurrentlyRunning(waitingTaskID)) {
            waitingTaskID = scheduler.scheduleSyncRepeatingTask(plugin,
                    new StartingGameTask(gameManager, this, messageHandler), 0L, 20L);
        }
    }

    public void stopWaitingTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (scheduler.isCurrentlyRunning(waitingTaskID)) {
            scheduler.cancelTask(waitingTaskID);
        }
    }

    public void initEndingPhase(Game game) {
        new GameEndingEvent(game).call();
        game.setState(GameState.ENDING);

        game.getCurrentPlayers().forEach(player -> {
            player.getInventory().clear();

            List<String> gameItemKeys = List.of("LEAVE", "PLAY_AGAIN");
            gameItemKeys.forEach(key ->
                    itemCache.find(key).ifPresent(gameItem -> {
                        if (gameItem.isEnabled()) {
                            player.getInventory().setItem(gameItem.getSlot(), gameItem.getItem());
                        }
                    })
            );
        });

        long endingTime = config.getLong("TIMES.ENDING");
        Bukkit.getScheduler().runTaskLater(plugin, () -> finishGame(game), 20L * endingTime);
    }

    public void startGame(Game game) {
        game.setState(GameState.PLAYING);
        game.setStartingTime(System.currentTimeMillis() + game.getMaxTime() * 1000L);

        sortPlayers(game);
        balanceTeams(game);
        teleportTeamToSpawn(game);

        game.getCurrentPlayers().forEach(this::giveSnowballs);

        new GameStartedEvent(game).call();
        schedulePlayingTask();
    }

    private void giveSnowballs(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        ItemStack item = new ItemStack(Material.SNOWBALL, 1);
        int amount = config.getInt("ARENA_SETTINGS.SNOWBALLS");

        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(item);
        }
    }

    public void schedulePlayingTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (!scheduler.isCurrentlyRunning(playingTaskID)) {
            Logger.info("Starting task");
            playingTaskID = scheduler.scheduleSyncRepeatingTask(plugin,
                    new PlayingGameTask(gameManager, this), 0L, 20L);
        }
    }

    public void stopPlayingTask() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (scheduler.isCurrentlyRunning(playingTaskID)) {
            Logger.info("Stopping task");
            scheduler.cancelTask(playingTaskID);
        }
    }

    public void finishGame(Game game) {
        new GameEndedEvent(game).call();

        game.setState(GameState.WAITING);

        for (Player player : game.getCurrentPlayers()) {
            handlePlayerQuit(player, game);
        }

        game.setFirstTeam(null);
        game.setSecondTeam(null);
        game.getCurrentPlayers().clear();
    }

    public void handlePlayerJoin(Player player, Game game) {
        //TODO: JUST FOR TESTING, NEEDS PROPER IMPLEMENTATION OF CONFIGURABLE TEAMS.
        if (game.getFirstTeam() == null) {
            Logger.info("Created dummy first team.");
            game.setFirstTeam(new Team("RED"));
            Team firstTeam = game.getFirstTeam();
            firstTeam.setSpawnLocation(game.getPointOne());
            firstTeam.setLives(game.getStartingLives());
        }

        if (game.getSecondTeam() == null) {
            Logger.info("Created dummy second team.");
            game.setSecondTeam(new Team("BLUE"));

            Team secondTeam = game.getSecondTeam();
            secondTeam.setSpawnLocation(game.getPointTwo());
            secondTeam.setLives(game.getStartingLives());
        }

        scoreboardManager.createScoreboard(player);

        playerCache.add(player.getUniqueId(), new PaintballPlayer(player));
        game.addPlayer(player);

        player.getInventory().clear();
        player.getEquipment().clear();
        player.updateInventory();

        player.setGameMode(GameMode.ADVENTURE); // SHRUG
        player.setExp(0);
        player.setLevel(0);
        player.setFoodLevel(20);
        player.setExhaustion(10);
        player.setHealth(20);
        player.setMaxHealth(20);
        player.setFlying(false);
        player.setAllowFlight(false);

        player.getActivePotionEffects().clear();
        player.teleport(game.getLobby());

        List<String> gameItemKeys = List.of("LEAVE", "PERKS", "HATS", "TEAM_SELECTOR");
        gameItemKeys.forEach(key ->
                itemCache.find(key).ifPresent(gameItem -> {
                    if (gameItem.isEnabled()) {
                        player.getInventory().setItem(gameItem.getSlot(), gameItem.getItem());
                    }
                })
        );

        if (game.getCurrentPlayersSize() >= game.getMinPlayers() && game.getState().equals(GameState.WAITING)) {
            initStartingPhase(game);
        }

        scoreboardManager.scheduleTask();
    }

    public void handlePlayerQuit(Player player, Game game) {
        scoreboardManager.deleteScoreboard(player);
        game.removePlayer(player);

        Location mainLobby = LocationUtils.deserialize(config.getString("MainLobby"));
        player.teleport(mainLobby);

        playerCache.find(player.getUniqueId())
                .ifPresent(paintballPlayer -> {
                    paintballPlayer.getSavedElements().restorePlayerElements(player);
                    player.updateInventory();
                    playerCache.remove(player.getUniqueId());
                });

        //HANDLE GAME STATES
        GameState state = game.getState();
        if (state == GameState.STARTING) {
            if (game.getCurrentPlayersSize() < game.getMinPlayers()) {
                // Return since game is no longer in starting state.
                notifyPlayers(game, messageHandler.getMessage(Messages.GAME_STARTING_CANCELLED));
                Logger.info("Cancelled start");
                game.setState(GameState.WAITING);
            }
            return;
        }

        if (state == GameState.PLAYING) {
            if (game.getCurrentPlayersSize() <= 1) {
                //Ending phase due to no players.
                initEndingPhase(game);
            }
        }
    }

    public void handlePlayerRespawn(Game game, Player player) {
        //TODO
    }

    public void handlePlayerDeath(Game game, Player dead, Player killer) {
        // Prevent spawn kill/kill abuse
        //int invulnerability = Integer.valueOf(config.getString("GAME.RESPAWN_INVULNERABILITY"));
        //if (dead.getRecentDeathMillis() == -1) return;

        Team targetTeam = gameManager.getPlayerTeam(dead.getPlayer());
        Team attackerTeam = gameManager.getPlayerTeam(killer.getPlayer());
        if (targetTeam.equals(attackerTeam)) {
            return;
        }

        if (config.getBoolean("GAME.DEATH_LIGHTNING")) {
            dead.getPlayer().getWorld().strikeLightningEffect(dead.getPlayer().getLocation());
        }

        targetTeam.decreaseLives();
        attackerTeam.increaseKills();

        new GameDeathEvent(dead, killer).call();
        dead.getPlayer().teleport(targetTeam.getSpawnLocation());
        handlePlayerRespawn(game, dead);

        int snowballs = config.getInt("ARENA_SETTINGS.PER_KILL_SNOWBALL");
        killer.getPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL, snowballs));
    }

    public void teleportTeamToSpawn(Game game) {
        game.getFirstTeam().getPlayers().forEach(teamPlayer -> Bukkit.getPlayer(teamPlayer).teleport(game.getPointOne()));
        game.getSecondTeam().getPlayers().forEach(teamPlayer -> Bukkit.getPlayer(teamPlayer).teleport(game.getPointTwo()));
    }

    public void playSound(Game game, Sound sound, float volume, float pitch) {
        game.getCurrentPlayers().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
    }

    public void notifyPlayers(Game game, String message) {
        game.getCurrentPlayers().forEach(player -> player.sendMessage(message));
    }

    public void notifyPlayers(Game game, List<String> messages) {
        messages.forEach(message -> notifyPlayers(game, message));
    }

    public void notifyPlayers(Game game, Consumer<Player> consumer) {
        game.getCurrentPlayers().forEach(consumer);
    }

    public void sortPlayers(Game game) {
        List<UUID> uuidList = new ArrayList<>(game.getCurrentPlayersUUID());
        Collections.shuffle(uuidList);

        // Sort with no selected team.
        if (!config.getBoolean("ARENA_SETTINGS.TEAM_SELECTION")) {
            int size = game.getCurrentPlayersSize();

            uuidList.subList(0, size / 2).forEach(uuid -> game.getFirstTeam().addPlayer(uuid));
            uuidList.subList(size / 2, size).forEach(uuid -> game.getSecondTeam().addPlayer(uuid));
            return;
        }

        // Assign players to teams based on their preferences
        for (UUID playerId : uuidList) {

            // Get the player's team preference
            Optional<PaintballPlayer> playerOptional = playerCache.find(playerId);
            if (playerOptional.isEmpty()) {
                Logger.info("SSomehow player is not in the playercache.");
                continue;
            }

            PaintballPlayer player = playerOptional.get();
            String teamPreference = player.getTeamChoice();

            // If no preference, assign to the team with fewer players
            if (teamPreference == null) {
                if (canSelectTeam(playerId, game.getFirstTeam().getName())) {
                    teamPreference = game.getFirstTeam().getName();
                } else {
                    teamPreference = game.getSecondTeam().getName();
                }
                player.setTeamChoice(teamPreference);
            }

            // Assign the player to their preferred team
            if (teamPreference.equals(game.getSecondTeam().getName())) {
                game.getSecondTeam().addPlayer(playerId);
            } else {
                game.getFirstTeam().addPlayer(playerId);
            }
        }
    }

    private void balanceTeams(Game game) {
        Team firstTeam = game.getFirstTeam();
        Team secondTeam = game.getSecondTeam();

        // Balance teams if one team has more players than the other
        while (Math.abs(firstTeam.getPlayersSize() - secondTeam.getPlayersSize()) > 1) {
            if (firstTeam.getPlayersSize() > secondTeam.getPlayersSize()) {
                movePlayer(firstTeam, secondTeam);
            } else {
                movePlayer(secondTeam, firstTeam);
            }
        }
    }

    private void movePlayer(Team fromTeam, Team toTeam) {
        if (!fromTeam.getPlayers().isEmpty()) {
            UUID playerToMove = fromTeam.getPlayers().iterator().next(); // Get the first player
            fromTeam.removePlayer(playerToMove);
            toTeam.addPlayer(playerToMove);
        }
    }

    public boolean canSelectTeam(UUID uuid, String team) {
        Optional<Game> optionalGame = gameManager.getPlayerGame(uuid);
        if (optionalGame.isEmpty()) return false;

        Game game = optionalGame.get();
        if (game.getCurrentPlayersSize() == 1) return true;

        Optional<PaintballPlayer> playerOptional = playerCache.find(uuid);
        // IDK if it'll work
        if (playerOptional.isEmpty()) return false;

        int teamPreferenceCount = countPlayersWithTeamPreference(team);

        int half = (game.getCurrentPlayersSize() + 1) / 2;
        return teamPreferenceCount < half;
    }

    private int countPlayersWithTeamPreference(String teamChoice) {
        int count = 0;
        for (PaintballPlayer cachedPlayer : playerCache.get().values()) {
            if (cachedPlayer.getTeamChoice() != null && cachedPlayer.getTeamChoice().equalsIgnoreCase(teamChoice)) {
                count++;
            }
        }
        return count;
    }

    public Team getWinner(Game game) {
        // If one team has no players, the other team automatically wins
        Team firstTeam = game.getFirstTeam();
        if (firstTeam.getPlayers().isEmpty()) return firstTeam;

        Team secondTeam = game.getSecondTeam();
        if (secondTeam.getPlayers().isEmpty()) return secondTeam;

        // Compare lives to determine the winner
        int firstTeamLives = firstTeam.getLives();
        int secondTeamLives = secondTeam.getLives();

        if (firstTeamLives == secondTeamLives) {
            return null; // Draw/tie
        }

        return firstTeamLives > secondTeamLives ? firstTeam : secondTeam;
    }
}
