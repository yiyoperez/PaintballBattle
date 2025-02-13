package pb.ajneb97.tasks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;
import pb.ajneb97.core.utils.message.MessageBuilder;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.SignManager;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.Messages;

import java.util.ArrayList;
import java.util.List;

public class SignUpdateTask extends BukkitRunnable {

    private final SignManager signManager;
    private final GameManager gameManager;
    private final MessageHandler messageHandler;

    //TODO: Replace task to game state update.
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
                String state = gameManager.getState(game);

                List<Placeholder> placeholderList = new ArrayList<>();
                placeholderList.add(new Placeholder("%arena%", arena));
                placeholderList.add(new Placeholder("%status%", state));
                placeholderList.add(new Placeholder("%max_players%", game.getMaxPlayers()));
                placeholderList.add(new Placeholder("%current_players%", game.getCurrentPlayersSize()));

                List<String> signFormat = new MessageBuilder(messageHandler)
                        .withMessage(Messages.SIGN_FORMAT)
                        .withPlaceholders(placeholderList)
                        .buildList();

                signFormat.forEach(line -> sign.setLine(signFormat.indexOf(line), line));

                sign.update();
            }
        });
    }
}
