package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.listeners.customevents.PreJoinGameEvent;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

@Command(name = "paintball join")
public class JoinCommand extends MainCommand {

    @Inject
    private GameManager gameManager;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Execute
    public void command(@Context Player player, @Arg("arena-name") String arenaName) {
        if (!gameManager.gameExists(arenaName)) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaDoesNotExists")));
            return;
        }

        Game game = gameManager.getGame(arenaName);
        new PreJoinGameEvent(game, player).call();
    }
}
