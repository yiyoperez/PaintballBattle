package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.managers.EditManager;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

@Command(name = "paintball edit")
public class EditCommand extends MainCommand {

    @Inject
    private GameManager gameManager;
    @Inject
    private EditManager editManager;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Execute
    @Permission("paintball.admin.edit")
    public void command(@Context Player player, @Arg("arena-name") String arenaName) {
        if (!gameManager.gameExists(arenaName)) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDoesNotExists")));
            return;
        }

        Game game = gameManager.getGame(arenaName);
        if (game.isEnabled()) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaMustBeDisabled")));
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
