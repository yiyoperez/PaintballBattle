package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

@Command(name = "paintball create")
@Description("Create a new arena.")
public class CreateCommand extends MainCommand {

    @Inject
    @Named("config")
    private YamlDocument config;

    @Inject
    private GameManager gameManager;

    @Inject
    private MessageHandler messageHandler;


    @Execute
    @Permission("paintball.admin.create")
    public void command(@Context Player player, @Arg("arenaName") String arenaName) {
        if (gameManager.gameExists(arenaName)) {
            messageHandler.sendMessage(player, Messages.ARENA_ALREADY_EXISTS);
            return;
        }
        if (!config.contains("MainLobby")) {
            messageHandler.sendMessage(player, Messages.NO_MAIN_LOBBY);
            return;
        }

        Placeholder placeholder = new Placeholder("%name%", arenaName);
        gameManager.addGame(new Game(arenaName));
        messageHandler.sendMessage(player, Messages.ARENA_CREATED, placeholder);
        messageHandler.sendMessage(player, Messages.ARENA_CREATED_EXTRA_INFO, placeholder);
    }
}
