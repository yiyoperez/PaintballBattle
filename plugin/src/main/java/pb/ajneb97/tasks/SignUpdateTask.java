package pb.ajneb97.tasks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.SignManager;
import pb.ajneb97.structures.Game;
import pb.ajneb97.utils.enums.Messages;

import java.util.List;

public class SignUpdateTask extends BukkitRunnable {

    private final SignManager signManager;
    private final GameManager gameManager;
    private final MessageHandler messageHandler;

    public SignUpdateTask(SignManager signManager, GameManager gameManager, MessageHandler messageHandler) {
        this.signManager = signManager;
        this.gameManager = gameManager;
        this.messageHandler = messageHandler;
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
                    case PLAYING -> messageHandler.getMessage(Messages.SIGN_STATUS_INGAME);
                    case WAITING -> messageHandler.getMessage(Messages.SIGN_STATUS_WAITING);
                    case ENDING -> messageHandler.getMessage(Messages.SIGN_STATUS_FINISHING);
                    case DISABLED -> messageHandler.getMessage(Messages.SIGN_STATUS_DISABLED);
                    case STARTING -> messageHandler.getMessage(Messages.SIGN_STATUS_STARTING);
                };

                List<String> signFormat = messageHandler.getRawStringList(Messages.SIGN_FORMAT.getPath()).stream().limit(4).toList();
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
