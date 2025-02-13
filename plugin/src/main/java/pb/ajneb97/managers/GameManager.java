package pb.ajneb97.managers;

import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.GameState;
import team.unnamed.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameManager {

    @Inject
    private PaintballBattle plugin;

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
        // Filter available games
        return getAvailableGames()
                .stream()
                .sorted(Comparator.comparingInt(Game::getCurrentPlayersSize).reversed())
                .findFirst();
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
}
