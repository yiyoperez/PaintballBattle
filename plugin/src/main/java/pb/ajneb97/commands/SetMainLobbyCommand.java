package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.commands.extra.PaintCommand;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.utils.LocationUtils;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.io.IOException;

@Command(name = "paintball setmainlobby")
public class SetMainLobbyCommand implements PaintCommand {

    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    private MessageHandler messageHandler;

    @Execute
    @Permission("paintball.command.setmainlobby")
    public void command(@Context Player player) {
        config.set("MainLobby", LocationUtils.serialize(player.getLocation()));
        try {
            config.save();
        } catch (IOException e) {
            // Could not save, send message?
            throw new RuntimeException(e);
        } finally {
            messageHandler.sendMessage(player, Messages.MAIN_LOBBY_DEFINED);
        }
    }
}
