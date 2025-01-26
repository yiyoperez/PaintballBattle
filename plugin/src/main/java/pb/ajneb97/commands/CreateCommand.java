package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.Game;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

@Command(name = "paintball create")
@Description("Create a new arena.")
public class CreateCommand extends MainCommand {

    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Inject
    private GameManager gameManager;


    @Execute
    @Permission("paintball.admin.create")
    public void command(@Context Player player, @Arg("arenaName") String arenaName) {
        if (gameManager.gameExists(arenaName)) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("arenaAlreadyExists")));
            return;
        }
        if (!config.contains("MainLobby")) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("noMainLobby")));
            return;
        }

        gameManager.addGame(new Game(arenaName));
        player.sendMessage(MessageUtils.translateColor(messages.getString("arenaCreated").replace("%name%", arenaName)));
        player.sendMessage(MessageUtils.translateColor(messages.getString("arenaCreatedExtraInfo").replace("%name%", arenaName)));
    }
}
