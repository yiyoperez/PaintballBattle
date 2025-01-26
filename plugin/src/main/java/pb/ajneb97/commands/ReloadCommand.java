package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import pb.ajneb97.core.utils.message.MessageUtils;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.io.IOException;

@Command(name = "paintball reload")
public class ReloadCommand extends MainCommand {

    @Inject
    @Named("shop")
    private YamlDocument shop;
    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Execute
    @Permission("paintball.admin.reload")
    public void command(@Context CommandSender sender) {
        try {
            messages.reload();
            config.reload();
            shop.reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // TODO: usage scoreboard manager.
        //plugin.recargarScoreboard();
        sender.sendMessage(MessageUtils.translateColor(messages.getString("configReloaded")));
    }
}
