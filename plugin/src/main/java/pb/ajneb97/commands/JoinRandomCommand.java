package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;
import pb.ajneb97.commands.extra.PaintCommand;
import pb.ajneb97.managers.GameManager;
import team.unnamed.inject.Inject;

@Command(name = "paintball joinrandom")
public class JoinRandomCommand implements PaintCommand {

    @Inject
    private GameManager gameManager;

    @Execute
    public void command(@Context Player player) {
        gameManager.attemptRandomJoin(player);
    }
}
