package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.listeners.customevents.GameLeaveEvent;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.Optional;

@Command(name = "paintball leave")
public class LeaveCommand extends MainCommand {

    @Inject
    private GameManager gameManager;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Execute
    public void command(@Context Player player) {
        if (!gameManager.isPlaying(player)) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("notInAGame")));
            return;
        }

        Optional<Game> optionalGame = gameManager.getPlayerGame(player);
        optionalGame.ifPresentOrElse(game -> new GameLeaveEvent(game, player).call(),
                () -> player.sendMessage(MessageUtils.translateColor(messages.getString("notInAGame"))));
    }
}
