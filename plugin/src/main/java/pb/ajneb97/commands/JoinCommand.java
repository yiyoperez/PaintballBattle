package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;
import pb.ajneb97.listeners.customevents.PreJoinGameEvent;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.game.Game;
import team.unnamed.inject.Inject;

@Command(name = "paintball join")
public class JoinCommand extends MainCommand {

    @Inject
    private GameManager gameManager;

    @Execute
    public void command(@Context Player player, @Arg Game game) {
        new PreJoinGameEvent(game, player).call();
    }
}
