package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.listeners.customevents.GameLeaveEvent;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

import java.util.Optional;

@Command(name = "paintball leave")
public class LeaveCommand extends MainCommand {

    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;

    @Execute
    public void command(@Context Player player) {
        if (!gameManager.isPlaying(player)) {
            messageHandler.sendMessage(player, Messages.NOT_IN_GAME);
            return;
        }

        Optional<Game> optionalGame = gameManager.getPlayerGame(player);
        optionalGame.ifPresentOrElse(game -> new GameLeaveEvent(game, player).call(),
                () -> messageHandler.sendMessage(player, Messages.NOT_IN_GAME));
    }
}
