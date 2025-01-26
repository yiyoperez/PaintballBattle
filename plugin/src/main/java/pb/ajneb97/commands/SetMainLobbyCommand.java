package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.utils.LocationUtils;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.io.IOException;

@Command(name = "paintball setmainlobby")
public class SetMainLobbyCommand extends MainCommand {

    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Execute
    @Permission("paintball.command.setmainlobby")
    public void command(@Context Player player) {
        config.set("MainLobby", LocationUtils.serialize(player.getLocation()));
        try {
            config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(MessageUtils.translateColor(messages.getString("mainLobbyDefined")));
    }
}
