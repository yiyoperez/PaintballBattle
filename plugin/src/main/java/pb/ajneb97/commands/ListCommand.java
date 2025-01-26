package pb.ajneb97.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import team.unnamed.inject.Inject;

import java.util.StringJoiner;

@Command(name = "paintball list")
public class ListCommand extends MainCommand {

    @Inject
    private GameManager gameManager;

    @Execute
    @Permission("paintball.admin.edit")
    public void command(@Context Player player) {
        if (gameManager.getGames().isEmpty()) {
            player.sendMessage("No hay partidas disponibles.");
            return;
        }

        StringJoiner joiner = new StringJoiner(", ");
        for (Game game : gameManager.getGames()) {
            String s = (game.isEnabled() ? "&a" : "&c") + game.getName();
            joiner.add(s);
        }
        player.sendMessage("Lista de arenas: " + MessageUtils.translateColor(joiner.toString()));
    }
}
