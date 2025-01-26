package pb.ajneb97.tasks;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.SignManager;
import pb.ajneb97.structures.Game;

import java.util.List;

public class SignUpdateTask extends BukkitRunnable {

    private final YamlDocument messages;
    private final SignManager signManager;
    private final GameManager gameManager;

    public SignUpdateTask(YamlDocument messages, SignManager signManager, GameManager gameManager) {
        this.messages = messages;
        this.signManager = signManager;
        this.gameManager = gameManager;
    }

    @Override
    @Deprecated
    public void run() {
        if (signManager.getLocationMap().isEmpty()) return;

        signManager.getLocationMap().forEach((arena, locationSet) -> {
            for (Location location : locationSet) {
                if (!location.getChunk().isLoaded()) return;

                Block block = location.getBlock();
                Sign sign = (Sign) block.getState();

                if (!gameManager.gameExists(arena)) continue;

                Game game = gameManager.getGame(arena);
                String state = switch (game.getState()) {
                    case PLAYING -> messages.getString("signStatusIngame");
                    case STARTING -> messages.getString("signStatusStarting");
                    case WAITING -> messages.getString("signStatusWaiting");
                    case DISABLED -> messages.getString("signStatusDisabled");
                    case ENDING -> messages.getString("signStatusFinishing");
                };

                List<String> signFormat = messages.getStringList("signFormat").stream().limit(4).toList();
                signFormat.forEach(line ->
                        sign.setLine(signFormat.indexOf(line), MessageUtils.translateColor(line
                                .replace("%arena%", arena)
                                .replace("%status%", state)
                                .replace("%max_players%", String.valueOf(game.getMaxPlayers()))
                                .replace("%current_players%", String.valueOf(game.getCurrentPlayersSize())))));

                sign.update();
            }
        });
    }
}
