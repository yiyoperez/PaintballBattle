package pb.ajneb97.managers;

import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.listeners.customevents.PreJoinGameEvent;
import pb.ajneb97.structures.PaintballPlayer;
import pb.ajneb97.structures.Team;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.structures.game.GameEditSessionBuilder;
import pb.ajneb97.utils.enums.GameState;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameManager {

    @Inject
    private PaintballBattle plugin;
    @Inject
    private MessageHandler messageHandler;

    private final Set<Game> gameSet = new HashSet<>();

    public Game getGame(String gameName) {
        for (Game game : gameSet) {
            if (game.getName().equalsIgnoreCase(gameName)) {
                return game;
            }
        }
        return null;
    }

    public void addGame(Game game) {
        gameSet.add(game);
    }

    public void removeGame(String gameName) {
        gameSet.removeIf(game -> game.getName().equalsIgnoreCase(gameName));
    }

    public Set<Game> getGames() {
        return gameSet;
    }

    public boolean gameExists(String gameName) {
        return gameSet
                .stream()
                .anyMatch(game -> game.getName().equalsIgnoreCase(gameName));
    }

    public boolean isGameAvailable(Game game) {
        return (game.getState() == GameState.WAITING || game.getState() == GameState.STARTING) && !game.isFull();
    }

    public Set<Game> getAvailableGames() {
        return gameSet
                .stream()
                .filter(this::isGameAvailable)
                .collect(Collectors.toSet());
    }

    public Optional<Game> getFirstAvailableGame() {
        return getAvailableGames().stream().max(Comparator.comparingInt(Game::getCurrentPlayersSize));
    }

    public boolean isPlaying(Player player) {
        return gameSet
                .stream()
                .filter(game -> game.getState() != GameState.DISABLED)
                .anyMatch(game -> game.contains(player));
    }

    public Optional<Game> getPlayerGame(Player player) {
        return gameSet
                .stream()
                .filter(game -> game.getState() != GameState.DISABLED)
                .filter(game -> game.contains(player))
                .findFirst();
    }

    public Optional<Game> getPlayerGame(UUID uuid) {
        return gameSet
                .stream()
                .filter(game -> game.getState() != GameState.DISABLED)
                .filter(game -> game.contains(uuid))
                .findFirst();
    }

    // idk why even have this
    public void deleteFile(String name) {
        File folder = new File(plugin.getDataFolder(), "arenas");
        try (Stream<Path> paths = Files.walk(folder.toPath())) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        String id = file.getFileName().toString().split("\\.")[0];
                        if (id.equalsIgnoreCase(name)) {
                            try {
                                plugin.getLogger().warning("Deleting file " + file.getFileName());
                                Files.deleteIfExists(file);
                            } catch (IOException ignored) {
                            }
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getState(Game game) {
        return switch (game.getState()) {
            case PLAYING -> messageHandler.getRawMessage(Messages.SIGN_STATUS_INGAME);
            case WAITING -> messageHandler.getRawMessage(Messages.SIGN_STATUS_WAITING);
            case ENDING -> messageHandler.getRawMessage(Messages.SIGN_STATUS_FINISHING);
            case DISABLED -> messageHandler.getRawMessage(Messages.SIGN_STATUS_DISABLED);
            case STARTING -> messageHandler.getRawMessage(Messages.SIGN_STATUS_STARTING);
        };
    }

    public void updateGame(String gameName, GameEditSessionBuilder session) {
        Game gameToUpdate = getGame(gameName);

        if (gameToUpdate != null) {
            // Remove the old game from the set
            gameSet.remove(gameToUpdate);

            // Build the updated game
            Game updatedGame = session.build();

            // Add the updated game back to the set
            gameSet.add(updatedGame);
        } else {
            Logger.info("Error attempting to update game.", Logger.LogType.WARNING);
            Logger.info("Game not found: " + gameName, Logger.LogType.WARNING);
        }
    }

    public Team getPlayerTeam(UUID uuid) {
        if (getPlayerGame(uuid).isEmpty()) return null;

        Game game = getPlayerGame(uuid).get();
        if (!game.contains(uuid)) return null;

        if (game.getFirstTeam().contains(uuid)) {
            return game.getFirstTeam();
        }
        if (game.getSecondTeam().contains(uuid)) {
            return game.getSecondTeam();
        }

        return null;
    }

    public Team getPlayerTeam(Player player) {
        return getPlayerTeam(player.getUniqueId());
    }

    public List<PaintballPlayer> getPlayerKills() {
        // Clone the players list and sort it by kills in descending order
//        return getPlayers().stream()
//                .sorted(Comparator.comparingInt(PaintballPlayer::getKills).reversed())
//                .toList();
        return null;
    }

    public void attemptRandomJoin(Player player) {
        Optional<Game> optionalGame = getFirstAvailableGame();
        optionalGame.ifPresentOrElse(game -> new PreJoinGameEvent(game, player).call(),
                () -> messageHandler.sendMessage(player, Messages.NO_ARENAS_AVAILABLE));
    }
}
