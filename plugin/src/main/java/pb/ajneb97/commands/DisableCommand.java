package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.GameState;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

@Command(name = "paintball disable")
public class DisableCommand extends MainCommand {

    @Inject
    private MessageHandler messageHandler;

    @Execute
    @Permission("paintball.admin.disable")
    public void command(@Context Player player, @Arg Game game) {
        if (!game.isEnabled()) {
            messageHandler.sendMessage(player, Messages.ARENA_ALREADY_DISABLED);
            return;
        }

        game.setEnabled(false);
        game.setState(GameState.DISABLED);
        messageHandler.sendMessage(player, Messages.ARENA_DISABLED, new Placeholder("%name%", game.getName()));
    }
}
