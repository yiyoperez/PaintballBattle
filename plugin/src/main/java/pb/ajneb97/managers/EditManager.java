package pb.ajneb97.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.entity.Player;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.inventories.EditInventory;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.structures.game.GameEdit;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditManager {

    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Inject
    private PaintballBattle plugin;
    @Inject
    private MessageHandler messageHandler;

    private final Map<UUID, GameEdit> gameEditMap = new HashMap<>();

    public boolean isArenaAvailable(Game arena) {
        return gameEditMap.values()
                .stream()
                .map(GameEdit::getArena)
                .map(Game::getName)
                .anyMatch(a -> a.equalsIgnoreCase(arena.getName()));
    }

    public void openEditInventory(Player admin) {
        new EditInventory(plugin, this, messageHandler).open(admin);
    }

    public GameEdit getGameEdit(Player player) {
        return gameEditMap.get(player.getUniqueId());
    }

    public void add(Player player, Game partida) {
        gameEditMap.put(player.getUniqueId(), new GameEdit(player, partida));
    }

    public void remove(Player player) {
        gameEditMap.remove(player.getUniqueId());
    }

    public boolean isEditing(Player player) {
        return gameEditMap.containsKey(player.getUniqueId());
    }

    public boolean isArenaEditAvailable(Game arena) {
        return gameEditMap.values().stream().anyMatch(game -> game.getArena().getName().equalsIgnoreCase(arena.getName()));
    }

    public Map<UUID, GameEdit> getGameEditMap() {
        return gameEditMap;
    }
}
