package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import pb.ajneb97.utils.enums.GameState;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

@Command(name = "paintball disable")
public class DisableCommand extends MainCommand {

    @Inject
    private GameManager gameManager;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Execute
    @Permission("paintball.admin.disable")
    public void command(@Context Player player, @Arg Game game) {
        if (!game.isEnabled()) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaAlreadyDisabled")));
            return;
        }

        game.setEnabled(false);
        game.setState(GameState.DISABLED);
        player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDisabled").replace("%name%", game.getName())));
    }
}
