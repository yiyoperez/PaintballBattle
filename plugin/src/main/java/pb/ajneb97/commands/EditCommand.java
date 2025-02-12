package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.managers.EditManager;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

@Command(name = "paintball edit")
public class EditCommand extends MainCommand {

    @Inject
    private GameManager gameManager;
    @Inject
    private EditManager editManager;
    @Inject
    private MessageHandler messageHandler;

    @Execute
    @Permission("paintball.admin.edit")
    public void command(@Context Player player, @Arg() Game game) {
        if (game.isEnabled()) {
            messageHandler.sendMessage(player, Messages.ARENA_MUST_BE_DISABLED);
            return;
        }

        //TODO
//        if (!editManager.isArenaAvailable(partida) && !editManager.isArenaEditAvailable(partida)) {
//            player.sendMessage("Another admin is editing that arena.");
//        }

        editManager.add(player, game);
        editManager.openEditInventory(player);
    }
}
