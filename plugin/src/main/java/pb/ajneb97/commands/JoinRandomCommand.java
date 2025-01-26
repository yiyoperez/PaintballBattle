package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
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

import java.util.Optional;

@Command(name = "paintball joinrandom")
public class JoinRandomCommand extends MainCommand {

    @Inject
    private GameManager gameManager;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Execute
    public void command(@Context Player player) {
        if (gameManager.isPlaying(player)) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("alreadyInArena")));
            return;
        }

        Optional<Game> optionalGame = gameManager.getFirstAvailableGame();
        optionalGame.ifPresentOrElse(game -> new PreJoinGameEvent(game, player).call(),
                () -> player.sendMessage(MessageUtils.translateColor(messages.getString("noArenasAvailable"))));
    }
}
