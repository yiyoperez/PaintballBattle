package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

@Command(name = "paintball delete")
public class DeleteCommand extends MainCommand {

    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;

    @Execute
    public void command(@Context Player player, @Arg Game game) {
        gameManager.removeGame(game.getName());
        messageHandler.sendMessage(player, Messages.ARENA_DELETED, new Placeholder("%name%", game.getName()));
    }
}
