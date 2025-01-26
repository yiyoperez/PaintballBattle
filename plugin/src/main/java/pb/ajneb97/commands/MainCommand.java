package pb.ajneb97.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import jdk.jfr.Description;
import org.bukkit.command.CommandSender;
import pb.ajneb97.core.utils.message.MessageHandler;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

@Command(name = "paintball", aliases = {"pb", "pball"})
@Description("Main command of PaintballBattle")
public class MainCommand {

    @Inject
    private MessageHandler messageHandler;

    @Inject
    @Named("messages")
    private YamlDocument messages;

    @Execute
    public void command(@Context CommandSender sender) {
        messageHandler.sendListMessage(sender, messages.getString("MAIN.HELP"));
    }
}
