package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.listeners.customevents.PreJoinGameEvent;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

import java.util.Optional;

@Command(name = "paintball joinrandom")
public class JoinRandomCommand extends MainCommand {

    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;

    @Execute
    public void command(@Context Player player) {
        if (gameManager.isPlaying(player)) {
            messageHandler.sendMessage(player, Messages.ALREADY_IN_ARENA);
            return;
        }

        Optional<Game> optionalGame = gameManager.getFirstAvailableGame();
        optionalGame.ifPresentOrElse(game -> new PreJoinGameEvent(game, player).call(),
                () -> messageHandler.sendMessage(player, Messages.NO_ARENAS_AVAILABLE));
    }
}
