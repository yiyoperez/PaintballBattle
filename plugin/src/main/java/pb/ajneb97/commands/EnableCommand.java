package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.commands.extra.PaintCommand;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

@Command(name = "paintball enable")
public class EnableCommand implements PaintCommand {

    @Inject
    private MessageHandler messageHandler;

    @Execute
    @Permission("paintball.admin.enable")
    public void command(@Context Player player, @Arg Game game) {
        if (game.isEnabled()) {
            messageHandler.sendMessage(player, Messages.ARENA_ALREADY_ENABLED);
            return;
        }
        if (game.getLobby() == null) {
            messageHandler.sendMessage(player, Messages.ENABLE_ARENA_LOBBY_ERROR);
            return;
        }
        if (game.getPointOne() == null || game.getPointTwo() == null) {
            messageHandler.sendMessage(player, Messages.ENABLE_ARENA_SPAWN_ERROR, new Placeholder("%number%", game.getPointTwo() == null ? 1 : 2));
            return;
        }
        /*if (game.getFirstTeam() == null || game.getSecondTeam() == null) {
            messageHandler.sendMessage(player, Messages.ENABLE_ARENA_TEAM_ERROR, new Placeholder("%number%", game.getFirstTeam() == null ? 1 : 2));
            return;
        }*/

        game.setEnabled(true);
        messageHandler.sendMessage(player, Messages.ARENA_ENABLED, new Placeholder("%name%", game.getName()));
    }
}
